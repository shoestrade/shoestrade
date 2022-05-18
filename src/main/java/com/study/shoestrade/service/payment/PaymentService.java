package com.study.shoestrade.service.payment;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.payment.Payment;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.dto.payment.request.PaymentRequestDto;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.payment.InsufficientPointException;
import com.study.shoestrade.exception.payment.MyTradeException;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.payment.PaymentRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

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
        String orderId = member.getName() + "_" + Objects.hashCode(requestDto.getName()+ member.getName()+LocalDateTime.now());

        if(trade.getSeller().equals(member)){
            throw new MyTradeException();
        }

        if(member.getPoint() < requestDto.getPoint()){
            throw new InsufficientPointException();
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


}
