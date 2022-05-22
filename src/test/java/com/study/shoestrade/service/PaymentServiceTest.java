package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.payment.PaymentMethod;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.payment.request.PaymentRequestDto;
import com.study.shoestrade.exception.payment.InsufficientPointException;
import com.study.shoestrade.exception.payment.MyTradeException;
import com.study.shoestrade.exception.payment.PaymentNotMatchedException;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
                .name("member1")
                .password("PW1")
                .point(100)
                .build();

        member2 = Member.builder()
                .id(2L)
                .email("member2")
                .name("member2")
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

    @Test
    @DisplayName("결제 정보 생성 시 포인트를 사용할 수 있다.")
    public void 결제_정보_생성_성공2() {
        // given
        PaymentRequestDto requestDto = PaymentRequestDto.builder()
                .method(PaymentMethod.CARD)
                .name("name")
                .price(99999)
                .point(1)
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

    @Test
    @DisplayName("trade id가 존재하지 않으면 TradeEmptyResultDataAccessException 예외가 발생한다.")
    public void 결제_정보_생성_실패1() {
        // given
        PaymentRequestDto requestDto = PaymentRequestDto.builder()
                .method(PaymentMethod.CARD)
                .name("name")
                .price(99999)
                .point(1)
                .tradeId(100L)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member1));
        given(tradeRepository.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> paymentService.createPayment(member1.getEmail(), requestDto))
                .isInstanceOf(TradeEmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("자기 자신이 올린 거래를 구매하려고 시도하면 MyTradeException 예외가 발생한다.")
    public void 결제_정보_생성_실패2() {
        // given
        PaymentRequestDto requestDto = PaymentRequestDto.builder()
                .method(PaymentMethod.CARD)
                .name("name")
                .price(99999)
                .point(1)
                .tradeId(100L)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member2));
        given(tradeRepository.findById(any())).willReturn(Optional.of(sell));

        // when, then
        assertThatThrownBy(() -> paymentService.createPayment(member2.getEmail(), requestDto))
                .isInstanceOf(MyTradeException.class);
    }

    @Test
    @DisplayName("회원이 가진 포인트보다 많은 포인트를 사용하려고 시도하면 InsufficientPointException 예외가 발생한다.")
    public void 결제_정보_생성_실패3() {
        // given
        PaymentRequestDto requestDto = PaymentRequestDto.builder()
                .method(PaymentMethod.CARD)
                .name("name")
                .price(99000)
                .point(1000)
                .tradeId(100L)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member1));
        given(tradeRepository.findById(any())).willReturn(Optional.of(sell));

        // when, then
        assertThatThrownBy(() -> paymentService.createPayment(member1.getEmail(), requestDto))
                .isInstanceOf(InsufficientPointException.class);
    }

    @Test
    @DisplayName("회원이 결제 시도한 금액과 포인트의 합이 거래의 가격과 일치하지 않으면 PaymentNotMatchedException 예외가 발생한다.")
    public void 결제_정보_생성_실패4() {
        // given
        PaymentRequestDto requestDto = PaymentRequestDto.builder()
                .method(PaymentMethod.CARD)
                .name("name")
                .price(9000)
                .point(100)
                .tradeId(100L)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member1));
        given(tradeRepository.findById(any())).willReturn(Optional.of(sell));

        // when, then
        assertThatThrownBy(() -> paymentService.createPayment(member1.getEmail(), requestDto))
                .isInstanceOf(PaymentNotMatchedException.class);
    }

}