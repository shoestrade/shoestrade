package com.study.shoestrade.repository.trade;

import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.response.TradeBreakdownCountDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TradeRepositoryCustom {

    // 거래 내역 조회
    Page<TradeLoadDto> findBreakdown(String email, TradeType tradeType, String state, Pageable pageable);

    /**
     * 사용자 이메일과 입찰 id로 입찰 정보 가져옴
     *
     * @param email     사용자 이메일
     * @param tradeId   입찰 id
     * @param tradeType 입찰 타입
     * @return 검색 결과
     */
    List<Trade> findByIdAndEmail(String email, Long tradeId, TradeType tradeType);

    /**
     * 즉시 거래가
     *
     * @param productId  상품 id
     * @param tradeState 입찰 상태(판매, 구매)
     * @return 검색 결과
     */
    List<TradeLoadDto> findInstantTrade(Long productId, TradeState tradeState);

    TradeBreakdownCountDto findBreakdownCount(String email, TradeType tradeType);

}
