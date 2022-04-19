package com.study.shoestrade.service.trade;

import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.TradeDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TradeService {

    /**
     * 입찰 등록
     *
     * @param email        사용자 이메일
     * @param tradeSaveDto 입찰 정보
     */
    void TradeSave(String email, TradeDto tradeSaveDto);


    /**
     * 사용자가 등록한 입찰 내역 검색
     *
     * @param email     사용자
     * @param tradeType 구매, 판매 구분
     * @param pageable  페이지 정보
     * @return 검색된 입찰 내역
     */
    Page<TradeLoadDto> findTradeByEmailAndTradeType(String email, TradeType tradeType, Pageable pageable);

    /**
     * 입찰 금액 수정
     *
     * @param email          사용자 이메일
     * @param tradeDto 수정할 입찰 정보
     */
    void updateTrade(String email, Long id, TradeDto tradeDto);

    /**
     * 입찰 삭제
     *
     * @param email          사용자 이메일
     * @param tradeDto 삭제할 입찰 정보
     */
    void deleteTrade(String email, TradeDto tradeDto);

}
