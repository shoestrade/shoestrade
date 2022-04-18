package com.study.shoestrade.service.trade;

import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.TradeSaveDto;
import com.study.shoestrade.dto.trade.request.TradeDeleteDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import com.study.shoestrade.dto.trade.request.TradeUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TradeService {

    /**
     * 입찰 등록
     *
     * @param email        사용자 이메일
     * @param tradeSaveDto 입찰 정보
     */
    void TradeSave(String email, TradeSaveDto tradeSaveDto);


    /**
     * 사용자가 등록한 입찰 내역 검색
     *
     * @param email     사용자
     * @param tradeType 구매, 판매 구분
     * @param pageable  페이지
     * @return 검색된 입찰 내역
     */
    Page<TradeLoadDto> findTradeByEmailAndTradeType(String email, TradeType tradeType, Pageable pageable);

    /**
     * 입찰 금액 수정
     *
     * @param email          사용자 이메일
     * @param tradeUpdateDto 수정할 입찰 정보
     */
    void updateTrade(String email, TradeUpdateDto tradeUpdateDto);

    /**
     * 입찰 삭제
     *
     * @param email          사용자 이메일
     * @param tradeDeleteDto 삭제할 입찰 정보
     */
    void deleteTrade(String email, TradeDeleteDto tradeDeleteDto);

}
