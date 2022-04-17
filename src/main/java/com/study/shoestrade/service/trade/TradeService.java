package com.study.shoestrade.service.trade;

import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.SalesTradeSaveDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDtoInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TradeService {

    /**
     * 판매 입찰 등록
     *
     * @param email             판매자 이메일
     * @param salesTradeSaveDto 판매 정보
     */
    void salesTradeSave(String email, SalesTradeSaveDto salesTradeSaveDto);


    /**
     * 사용자가 등록한 입찰 내역 검색
     *
     * @param email     사용자
     * @param tradeType 구매, 판매 구분
     * @param pageable  페이지
     * @return 검색된 입찰 내역
     */
    Page<TradeLoadDtoInterface> findTradeByEmailAndTradeType(String email, TradeType tradeType, Pageable pageable);
}
