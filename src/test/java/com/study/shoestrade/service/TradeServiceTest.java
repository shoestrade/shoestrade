package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.TradeDto;
import com.study.shoestrade.dto.trade.response.TradeDoneDto;
import com.study.shoestrade.dto.trade.response.TradeTransactionDto;
import com.study.shoestrade.exception.payment.MyTradeException;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.exception.trade.WrongTradeTypeException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.product.ProductRepository;
import com.study.shoestrade.repository.product.ProductSizeRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import com.study.shoestrade.service.member.MailService;
import com.study.shoestrade.service.trade.TradeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @InjectMocks
    private TradeServiceImpl tradeService;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private ProductSizeRepository productSizeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MailService mailService;

    Member member, member2;
    ProductSize productSize;
    TradeDto tradeSaveDto;
    Trade sell, purchase;

    @BeforeEach
    public void init(){
        member = Member.builder().id(1L).email("이메일").build();
        member2 = Member.builder()
                .id(2L)
                .email("member2")
                .build();

        productSize = ProductSize.builder().id(1L).size(255).build();

        tradeSaveDto = TradeDto.builder()
                .price(1000)
                .productSizeId(1L)
                .tradeType(TradeType.SELL)
                .build();

        sell = Trade.builder()
                .id(1L)
                .price(tradeSaveDto.getPrice())
                .productSize(productSize)
                .tradeType(tradeSaveDto.getTradeType())
                .tradeState(TradeState.SELL)
                .seller(member)
                .build();

        purchase = Trade.builder()
                .id(2L)
                .price(100000)
                .productSize(productSize)
                .tradeType(TradeType.PURCHASE)
                .tradeState(TradeState.PURCHASE)
                .purchaser(member2)
                .build();

    }

    @Test
    @DisplayName("입찰_등록_테스트")
    public void 입찰_등록() {

        // given
        given(tradeRepository.save(any())).willReturn(sell);
        given(productSizeRepository.findById(any())).willReturn(Optional.ofNullable(productSize));
        given(memberRepository.findByEmail(any())).willReturn(Optional.ofNullable(member));

        // when
        // then
        assertThatCode(() -> tradeService.TradeSave(member.getEmail(), tradeSaveDto)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("입찰_수정_테스트")
    public void 입찰_수정() {
        // given
        ArrayList<Trade> trades = new ArrayList<>(List.of(sell));
        given(tradeRepository.findByIdAndEmail(any(), any(), any())).willReturn(trades);
        TradeDto tradeUpdateDto = TradeDto.builder().price(1000).tradeType(TradeType.SELL).build();

        // when
        tradeService.updateTrade("이메일", 1L, tradeUpdateDto);

        // then
        assertThat(sell.getPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("입찰_수정_오류_테스트")
    public void 입찰_수정_오류() {
        // given
        ArrayList<Trade> trades = new ArrayList<>();
        given(tradeRepository.findByIdAndEmail(any(), any(), any())).willReturn(trades);

        // when
        // then
        assertThatThrownBy(() -> tradeService.updateTrade("이메일", 1L,
                TradeDto.builder().price(1000).tradeType(TradeType.SELL).build())).isInstanceOf(TradeEmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("입찰_삭제_테스트")
    public void 입찰_삭제() {
        // given
        ArrayList<Trade> trades = new ArrayList<>(List.of(sell));
        given(tradeRepository.findByIdAndEmail(any(), any(), any())).willReturn(trades);
        willDoNothing().given(tradeRepository).delete(any());
        TradeDto tradeDeleteDto = TradeDto.builder().id(sell.getId()).tradeType(TradeType.SELL).build();

        // when
        // then
        assertThatCode(() -> tradeService.deleteTrade("이메일", tradeDeleteDto)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("입찰_삭제_오류_테스트")
    public void 입찰_삭제_오류() {
        // given
        ArrayList<Trade> trades = new ArrayList<>();
        given(tradeRepository.findByIdAndEmail(any(), any(), any())).willReturn(trades);
        TradeDto tradeDeleteDto = TradeDto.builder().id(sell.getId()).tradeType(TradeType.SELL).build();

        // when
        // then
        assertThatThrownBy(() -> tradeService.deleteTrade("이메일", tradeDeleteDto)).isInstanceOf(TradeEmptyResultDataAccessException.class);
    }


    @Test
    @DisplayName("체결_거래_내역_테스트")
    public void 최근_체결_거래_내역() {
        // given
        LocalDateTime localDateTime = LocalDateTime.now();
        TradeDoneDto tradeDoneDto = TradeDoneDto.builder()
                .price(1000)
                .size(250)
                .tradeDate(localDateTime)
                .build();
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<TradeDoneDto> page = new PageImpl<>(new ArrayList<>(List.of(tradeDoneDto)), pageRequest, 1);

        given(tradeRepository.findDoneTrade(any(), any())).willReturn(page);
        given(productRepository.findById(any())).willReturn(Optional.ofNullable(Product.builder().id(1L).build()));

        // when
        Page<TradeDoneDto> resultPage = tradeService.findDoneTrade(1L, pageRequest);

        // then
        assertThat(resultPage).isEqualTo(page);
    }


    @Test
    @DisplayName("입찰_내역_테스트")
    public void 입찰_내역() {
        // given
        TradeTransactionDto tradeTransactionDto = TradeTransactionDto.builder()
                .price(1000)
                .size(250)
                .quantity(1L)
                .build();
        PageRequest pageRequest = PageRequest.of(0, 3);
        Page<TradeTransactionDto> page = new PageImpl<>(new ArrayList<>(List.of(tradeTransactionDto)), pageRequest, 1);

        given(tradeRepository.findTransactionTrade(any(), any(), any())).willReturn(page);
        given(productRepository.findById(any())).willReturn(Optional.ofNullable(Product.builder().id(1L).build()));

        // when
        Page<TradeTransactionDto> resultPage = tradeService.findTransactionTrade(1L, "sell", pageRequest);

        // then
        assertThat(resultPage).isEqualTo(page);
    }

    @Test
    @DisplayName("구매 입찰에 올라온 거래를 판매 체결 요청하면 거래의 상태가 READY로 변경된다.")
    public void 즉시_판매_체결_성공() {
        // given, mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(tradeRepository.findById(any())).willReturn(Optional.of(purchase));

        // when
        tradeService.sellTrade(member.getEmail(), purchase.getId());

        // then
        assertThat(purchase.getTradeState()).isEqualTo(TradeState.READY);
        assertThat(purchase.getSeller()).isEqualTo(member);
        assertThat(purchase.getClaimDueDate()).isNotNull();
    }

    @Test
    @DisplayName("요청한 거래의 상태가 PURCHASE가 아니면 WrongTradeTypeException 예외가 발생한다.")
    public void 즉시_판매_체결_실패1() {
        // given
        Trade trade = Trade.builder()
                .id(3L)
                .tradeState(TradeState.SELL)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(tradeRepository.findById(any())).willReturn(Optional.of(trade));

        // when, then
        assertThatThrownBy(() -> tradeService.sellTrade(member.getEmail(), trade.getId()))
                .isInstanceOf(WrongTradeTypeException.class);
    }

    @Test
    @DisplayName("요청한 거래가 자신의 거래이면 MyTradeException 예외가 발생한다.")
    public void 즉시_판매_체결_실패2() {
        // given
        Trade trade = Trade.builder()
                .id(3L)
                .tradeState(TradeState.PURCHASE)
                .tradeType(TradeType.PURCHASE)
                .purchaser(member)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(tradeRepository.findById(any())).willReturn(Optional.of(trade));

        // when, then
        assertThatThrownBy(() -> tradeService.sellTrade(member.getEmail(), trade.getId()))
                .isInstanceOf(MyTradeException.class);
    }

}