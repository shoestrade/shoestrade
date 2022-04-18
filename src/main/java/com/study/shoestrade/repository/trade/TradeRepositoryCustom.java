package com.study.shoestrade.repository.trade;

import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TradeRepositoryCustom {

    /**
     * 입찰 검색
     *
     * @param email     사용자 이메일
     * @param tradeType 구매, 판매
     * @return 검색된 입찰 내역
     */
    Page<TradeLoadDto> findTradeByEmailAndTradeType(String email, TradeType tradeType, Pageable pageable);

    /**
     * 사용자 이메일과 입찰 id로 입찰 정보 가져옴
     *
     * @param email     사용자 이메일
     * @param tradeId   입찰 id
     * @param tradeType 입찰 타입
     * @return 검색 결과
     */
    List<Trade> findByIdAndEmail(String email, Long tradeId, TradeType tradeType);
}
