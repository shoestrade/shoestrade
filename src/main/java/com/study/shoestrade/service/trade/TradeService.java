package com.study.shoestrade.service.trade;

import com.study.shoestrade.dto.trade.request.TradeDto;
import com.study.shoestrade.dto.trade.response.TradeBreakdownCountDto;
import com.study.shoestrade.dto.trade.response.TradeDoneDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import com.study.shoestrade.dto.trade.response.TradeTransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TradeService {

    /**
     * 입찰 등록
     *
     * @param email        사용자 이메일
     * @param tradeSaveDto 입찰 정보
     */
    void TradeSave(String email, TradeDto tradeSaveDto);

    // 거래 내역 수 조회
    TradeBreakdownCountDto getBreakdownCount(String email, String tradeType);

    // 거래 내역 조회
    Page<TradeLoadDto> getBreakdown(String email, String tradeType, String state, Pageable pageable);

    /**
     * 입찰 금액 수정
     *
     * @param email    사용자 이메일
     * @param tradeDto 수정할 입찰 정보
     */
    void updateTrade(String email, Long id, TradeDto tradeDto);

    /**
     * 입찰 삭제
     *
     * @param email    사용자 이메일
     * @param tradeDto 삭제할 입찰 정보
     */
    void deleteTrade(String email, TradeDto tradeDto);

    /**
     * 상품의 체결 거래 내역
     *
     * @param productId 상품 id
     * @param pageable  페이지 정보
     * @return 검색 결과
     */
    Page<TradeDoneDto> findDoneTrade(Long productId, Pageable pageable);

    /**
     * 상품의 입찰 내역
     *
     * @param productId  상품 id
     * @param tradeState 입찰 상태(판매, 구매)
     * @param pageable   페이지 정보
     * @return 검색 결과
     */
    Page<TradeTransactionDto> findTransactionTrade(Long productId, String tradeState, Pageable pageable);

    /**
     * 즉시 거래가
     *
     * @param productId  상품 id
     * @param tradeState 입찰 상태(판매, 구매)
     * @return 검색 결과
     */
    List<TradeLoadDto> findInstantTrade(Long productId, String tradeState);
}
