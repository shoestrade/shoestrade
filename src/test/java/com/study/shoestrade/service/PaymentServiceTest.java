package com.study.shoestrade.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.payment.Payment;
import com.study.shoestrade.domain.payment.PaymentMethod;
import com.study.shoestrade.domain.payment.PaymentStatus;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.payment.request.PaymentRequestDto;
import com.study.shoestrade.dto.payment.request.PaymentVerifyRequestDto;
import com.study.shoestrade.exception.payment.InsufficientPointException;
import com.study.shoestrade.exception.payment.MyTradeException;
import com.study.shoestrade.exception.payment.PaymentPriceNotMatchedException;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.payment.PaymentRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import com.study.shoestrade.service.payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    @DisplayName("?????? ?????? ????????? ???????????? trade??? ????????? READY??? ????????????.")
    public void ??????_??????_??????_??????1() {
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
    @DisplayName("?????? ?????? ?????? ??? ???????????? ????????? ??? ??????.")
    public void ??????_??????_??????_??????2() {
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
    @DisplayName("trade id??? ???????????? ????????? TradeEmptyResultDataAccessException ????????? ????????????.")
    public void ??????_??????_??????_??????1() {
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
    @DisplayName("?????? ????????? ?????? ????????? ??????????????? ???????????? MyTradeException ????????? ????????????.")
    public void ??????_??????_??????_??????2() {
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
    @DisplayName("????????? ?????? ??????????????? ?????? ???????????? ??????????????? ???????????? InsufficientPointException ????????? ????????????.")
    public void ??????_??????_??????_??????3() {
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
    @DisplayName("????????? ?????? ????????? ????????? ???????????? ?????? ????????? ????????? ???????????? ????????? PaymentNotMatchedException ????????? ????????????.")
    public void ??????_??????_??????_??????4() {
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
                .isInstanceOf(PaymentPriceNotMatchedException.class);
    }

    /*
    @Test
    public void ??????_??????_??????_??????() throws IamportResponseException, IOException {
        // given
        PaymentVerifyRequestDto requestDto = PaymentVerifyRequestDto.builder()
                .orderId("orderId")
                .impId("impId")
                .tradeId(sell.getId())
                .build();

        Payment payment = Payment.builder()
                .id(201L)
                .orderId("orderId")
                .method(PaymentMethod.CARD)
                .price(sell.getPrice())
                .trade(sell)
                .status(PaymentStatus.READY)
                .build();



        // mocking
        given(memberRepository.findByEmail("member1")).willReturn(Optional.of(member1));
        given(tradeRepository.findByIdAndPurchaser(101L, member1)).willReturn(Optional.of(sell));
        given(paymentRepository.findByOrderIdAndTrade("orderId", sell)).willReturn(Optional.of(payment));

        // when
        paymentService.verifyPayment("member1", requestDto);

        // then
    }
    */
}