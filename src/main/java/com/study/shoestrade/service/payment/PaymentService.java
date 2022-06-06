package com.study.shoestrade.service.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.payment.Payment;
import com.study.shoestrade.domain.payment.PaymentMethod;
import com.study.shoestrade.domain.payment.PaymentStatus;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.dto.payment.request.PaymentRequestDto;
import com.study.shoestrade.dto.payment.request.PaymentVerifyRequestDto;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.payment.*;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.payment.PaymentRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;
    private final PaymentRepository paymentRepository;

    @Value("${spring.pgmodule.app-id}")
    private String apiKey;
    @Value("${spring.pgmodule.secret-key}")
    private String apiSecret;

    // 결제 정보 생성
    public String createPayment(String email, PaymentRequestDto requestDto){
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Trade trade = tradeRepository.findById(requestDto.getTradeId()).orElseThrow(() -> new TradeEmptyResultDataAccessException(requestDto.getTradeId().toString(), 1));
        String orderId =createOrderId(member.getName(), requestDto.getName());

        if(trade.getSeller().equals(member)){
            throw new MyTradeException();
        }

        if(member.getPoint() < requestDto.getPoint()){
            throw new InsufficientPointException();
        }

        if(trade.getPrice() != requestDto.getPrice() + requestDto.getPoint()){
            throw new PaymentPriceNotMatchedException();
        }

        trade.changeState(TradeState.READY);
        trade.changePurchaser(member);

        Payment payment = Payment.builder()
                .trade(trade)
                .orderId(orderId)
                .method(requestDto.getMethod())
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .point(requestDto.getPoint())
                .build();

        paymentRepository.save(payment);
        return orderId;
    }

    // 결제 검증
    public void verifyPayment(String email, PaymentVerifyRequestDto requestDto) throws IamportResponseException, IOException {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Trade trade = tradeRepository.findByIdAndPurchaser(requestDto.getTradeId(), member)
                .orElseThrow(() -> new TradeEmptyResultDataAccessException(requestDto.getTradeId().toString(), 1));
        Payment payment = paymentRepository.findByOrderIdAndTrade(requestDto.getOrderId(), trade)
                .orElseThrow(() -> new PaymentNotFoundException("orderId = " + requestDto.getOrderId() + ", tradeId = " + requestDto.getTradeId()));

        IamportClient iamportClient = new IamportClient(apiKey, apiSecret);
        IamportResponse<com.siot.IamportRestClient.response.Payment> paymentResponse = iamportClient.paymentByImpUid(requestDto.getImpId());

        com.siot.IamportRestClient.response.Payment paymentData = paymentResponse.getResponse();

        IamportPaymentIsNotNull(paymentData, requestDto.getImpId());
        checkPaymentOrderId(paymentData, payment);

        if(payment.getPrice() != paymentData.getAmount().intValue()) {
            throw new InsufficientPointException();
        }

        PaymentMethod method = PaymentMethod.valueOf(paymentData.getPayMethod().toUpperCase());
        PaymentStatus status = PaymentStatus.valueOf(paymentData.getStatus().toUpperCase());

        if(!method.equals(payment.getMethod())) {
            throw new PaymentMethodNotConsistException();
        }
        if(!status.equals(PaymentStatus.PAID)) {
            throw new PaymentUnpaidException();
        }

        LocalDateTime paidAt = paymentData.getPaidAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        payment.successPayment(requestDto.getImpId(), method, status, paidAt);
        member.usePoint(payment.getPoint());
        trade.changeState(TradeState.COMPLETE);
    }

    // 결제 취소
    public int cancelPayment(Payment payment) throws IamportResponseException, IOException {
        IamportClient iamportClient = new IamportClient(apiKey, apiSecret);
        IamportResponse<com.siot.IamportRestClient.response.Payment> paymentResponse = iamportClient.paymentByImpUid(payment.getImpId());

        com.siot.IamportRestClient.response.Payment paymentData = paymentResponse.getResponse();

        IamportPaymentIsNotNull(paymentData, payment.getImpId());
        checkPaymentOrderId(paymentData, payment);

        if(payment.getPrice() - paymentData.getCancelAmount().intValue() <= 0){
            throw new PaymentCanceledException();
        }

        try{
            String accessToken = requestToken();

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            JSONObject body = new JSONObject();
            body.put("imp_uid", payment.getImpId());
            body.put("checksum", (double)payment.getPrice());

            HttpEntity<JSONObject> entity = new HttpEntity<>(body, headers);
            ResponseEntity<JSONObject> response = restTemplate.postForEntity("https://api.iamport.kr/payments/cancel", entity, JSONObject.class);

            int code = (int)response.getBody().get("code");

            if(code == 0){
                payment.changeStatus(PaymentStatus.CANCELLED);
                return payment.getPoint();
            }else{
                throw new PaymentCancelFailureException();
            }

        } catch (RestClientException e){
            throw new PaymentRestTemplateException();
        }
    }

    // 토큰 발급 요청
    private String requestToken(){
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject body = new JSONObject();
            body.put("imp_key", apiKey);
            body.put("imp_secret", apiSecret);

            HttpEntity<JSONObject> entity = new HttpEntity<>(body, headers);
            ResponseEntity<JSONObject> token = restTemplate.postForEntity("https://api.iamport.kr/users/getToken", entity, JSONObject.class);

            return (String) ((HashMap<?, ?>) token.getBody().get("response")).get("access_token");
        } catch (RestClientException e){
            throw new PaymentRestTemplateException();
        }
    }

    private String createOrderId(String memberName, String orderName){
        LocalDateTime now = LocalDateTime.now();
        int hash = 17;
        hash = 31 * hash + memberName.hashCode();
        hash = 31 * hash + orderName.hashCode();
        hash = 31 * hash + now.hashCode();
        hash = hash & 0x7fffffff;

        return "ST" + now.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" + hash;
    }

    private void IamportPaymentIsNotNull(com.siot.IamportRestClient.response.Payment paymentData, String impId){
        if(Objects.isNull(paymentData)) {
            throw new PaymentNotFoundException("impId가 " + impId);
        }
    }

    private void checkPaymentOrderId(com.siot.IamportRestClient.response.Payment paymentData, Payment payment){
        if(!payment.getOrderId().equals(paymentData.getMerchantUid())) {
            throw new PaymentOrderIdNotConsistException();
        }
    }


}
