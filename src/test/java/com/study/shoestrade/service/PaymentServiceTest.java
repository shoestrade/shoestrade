package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.payment.PaymentMethod;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.payment.request.PaymentRequestDto;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.payment.PaymentRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import com.study.shoestrade.service.payment.PaymentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    TradeRepository tradeRepository;
    @Mock
    PaymentRepository paymentRepository;

    Member member1, member2;
    Trade sell;

    @BeforeEach
    public void init(){
        member1 = Member.builder()
                .id(1L)
                .email("member1")
                .password("PW1")
                .point(100)
                .build();

        member2 = Member.builder()
                .id(2L)
                .email("member2")
                .password("PW2")
                .build();

        sell = Trade.builder()
                .id(101L)
                .price(100000)
                .tradeType(TradeType.SELL)
                .seller(member2)
                .tradeState(TradeState.SELL)
                .build();

    }

    @Test
    @DisplayName("결제 정보 생성을 요청하면 trade의 상태가 READY로 변경된다.")
    public void 결제_정보_생성_성공1() {
        // given
        PaymentRequestDto requestDto = PaymentRequestDto.builder()
                .method(PaymentMethod.CARD)
                .name("name")
                .price(100000)
                .point(0)
                .tradeId(100L)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member1));
        given(tradeRepository.findById(any())).willReturn(Optional.of(sell));

        // when
        paymentService.createPayment(member1.getEmail(), requestDto);

        // then
        assertThat(sell.getTradeState()).isEqualTo(TradeState.READY);
    }
}