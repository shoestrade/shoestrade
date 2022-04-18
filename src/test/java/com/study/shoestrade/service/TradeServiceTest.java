package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.TradeDeleteDto;
import com.study.shoestrade.dto.trade.request.TradeSaveDto;
import com.study.shoestrade.dto.trade.request.TradeUpdateDto;
import com.study.shoestrade.dto.trade.response.QTradeLoadDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.product.ProductSizeRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import com.study.shoestrade.service.trade.TradeService;
import com.study.shoestrade.service.trade.TradeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.study.shoestrade.domain.product.QProduct.product;
import static com.study.shoestrade.domain.trade.QTrade.trade;
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
    private MemberRepository memberRepository;


    Member member = Member.builder().id(1L).email("이메일").build();
    ProductSize productSize = ProductSize.builder().id(1L).size(255).build();
    TradeSaveDto tradeSaveDto = TradeSaveDto.builder()
            .price(1000)
            .productSizeId(1L)
            .tradeType(TradeType.SELL)
            .build();

    Trade trade = Trade.builder()
            .id(1L)
            .price(tradeSaveDto.getPrice())
            .productSize(productSize)
            .tradeType(tradeSaveDto.getTradeType())
            .tradeState(TradeState.SELL)
            .seller(member)
            .build();

    @Test
    @DisplayName("입찰_등록_테스트")
    public void 입찰_등록() {

        // given
        given(tradeRepository.save(any())).willReturn(trade);
        given(productSizeRepository.findById(any())).willReturn(Optional.ofNullable(productSize));
        given(memberRepository.findByEmail(any())).willReturn(Optional.ofNullable(member));

        // when
        // then
        assertThatCode(() -> tradeService.TradeSave(member.getEmail(), tradeSaveDto)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("입찰_검색_테스트")
    public void 입찰_검색() {

        // given
        PageRequest pageRequest = PageRequest.of(0, 3);

        Page<TradeLoadDto> page = new PageImpl<>(new ArrayList<>(List.of(new TradeLoadDto(1L, "입찰", 1000))), pageRequest, 1);

        given(tradeRepository.findTradeByEmailAndTradeType(any(), any(), any())).willReturn(page);

        // when
        Page<TradeLoadDto> findPage = tradeService.findTradeByEmailAndTradeType("이메일", TradeType.SELL, pageRequest);

        // then
        assertThat(findPage).isEqualTo(page);
    }

    @Test
    @DisplayName("입찰_수정_테스트")
    public void 입찰_수정() {
        // given
        ArrayList<Trade> trades = new ArrayList<>(List.of(trade));
        given(tradeRepository.findByIdAndEmail(any(), any(), any())).willReturn(trades);
        TradeUpdateDto tradeUpdateDto = TradeUpdateDto.builder().id(1L).price(1000).tradeType(TradeType.SELL).build();

        // when
        tradeService.updateTrade("이메일", tradeUpdateDto);

        // then
        assertThat(trade.getPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("입찰_수정_오류_테스트")
    public void 입찰_수정_오류() {
        // given
        ArrayList<Trade> trades = new ArrayList<>();
        given(tradeRepository.findByIdAndEmail(any(), any(), any())).willReturn(trades);

        // when
        // then
        assertThatThrownBy(() -> tradeService.updateTrade("이메일",
                TradeUpdateDto.builder().id(1L).price(1000).tradeType(TradeType.SELL).build())).isInstanceOf(TradeEmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("입찰_삭제_테스트")
    public void 입찰_삭제() {
        // given
        ArrayList<Trade> trades = new ArrayList<>(List.of(trade));
        given(tradeRepository.findByIdAndEmail(any(), any(), any())).willReturn(trades);
        willDoNothing().given(tradeRepository).delete(any());
        TradeDeleteDto tradeDeleteDto = TradeDeleteDto.builder().id(trade.getId()).tradeType(TradeType.SELL).build();

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
        TradeDeleteDto tradeDeleteDto = TradeDeleteDto.builder().id(trade.getId()).tradeType(TradeType.SELL).build();

        // when
        // then
        assertThatThrownBy(() -> tradeService.deleteTrade("이메일", tradeDeleteDto)).isInstanceOf(TradeEmptyResultDataAccessException.class);
    }
}