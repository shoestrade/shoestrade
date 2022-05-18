package com.study.shoestrade.service.payment;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.payment.Payment;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.dto.payment.request.PaymentRequestDto;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.payment.InsufficientPointException;
import com.study.shoestrade.exception.payment.MyTradeException;
import com.study.shoestrade.exception.payment.PaymentNotMatchedException;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.payment.PaymentRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final MemberRepository memberRepository;
    private final TradeRepository tradeRepository;
    private final PaymentRepository paymentRepository;

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
            throw new PaymentNotMatchedException();
        }

        trade.changeState(TradeState.READY);

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

    protected String createOrderId(String memberName, String orderName){
        LocalDateTime now = LocalDateTime.now();
        int hash = 17;
        hash = 31 * hash + memberName.hashCode();
        hash = 31 * hash + orderName.hashCode();
        hash = 31 * hash + now.hashCode();
        hash = hash & 0x7fffffff;

        return "ST" + now.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" + hash;
    }


}
