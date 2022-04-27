package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.dto.trade.request.TradeDto;
import com.study.shoestrade.service.trade.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradeController {

    private final ResponseService responseService;

    private final TradeService tradeService;

    /**
     * 입찰 등록
     *
     * @param email    사용자 이메일
     * @param tradeDto 입찰 정보
     * @return 성공 결과
     */
    @PostMapping
    public Result salesTradeSave(@LoginMember String email, @RequestBody TradeDto tradeDto) {
        tradeService.TradeSave(email, tradeDto);
        return responseService.getSuccessResult();
    }

    /**
     * 거래 내역 수 조회
     * @param email : accessToken email
     * @param tradeType : 거래 타입 {sell : 구매 거래 내역, purchase : 판매 거래 내역}
     * @return
     */
    @GetMapping("/{tradeType}/count")
    public Result getBreakdownCount(@LoginMember String email, @PathVariable("tradeType") String tradeType){
        return responseService.getSingleResult(tradeService.getBreakdownCount(email, tradeType));
    }

    /**
     * 거래 내역 조회
     * @param email : accessToken email
     * @param tradeType : 거래 타입 {sell : 구매 거래 내역, purchase : 판매 거래 내역}
     * @param state : 거래 상태 {bid : 입찰, progress : 진행 중, done : 종료(완료)}
     * @param pageable : 페이징
     * @return
     */
    @GetMapping("/{tradeType}/{state}")
    public Result getBreakdown(@LoginMember String email, @PathVariable("tradeType") String tradeType, @PathVariable("state") String state, Pageable pageable){
        return responseService.getSingleResult(tradeService.getBreakdown(email, tradeType, state, pageable));
    }

    /**
     * 입찰 금액 수정
     *
     * @param email    사용자 이메일
     * @param tradeId  거래 id
     * @param tradeDto 수정할 입찰 정보
     * @return 성공 결과
     */
    @PostMapping("/{tradeId}")
    public Result updateTrade(@LoginMember String email, @PathVariable("tradeId") Long tradeId, @RequestBody TradeDto tradeDto) {
        tradeService.updateTrade(email, tradeId, tradeDto);
        return responseService.getSuccessResult();
    }

    /**
     * 입찰 삭제
     *
     * @return 성공 결과
     */
    @DeleteMapping("/{tradeId}")
    public Result deleteTrade(@LoginMember String email, @PathVariable("tradeId") Long tradeId, @RequestBody TradeDto tradeDto) {
        tradeService.deleteTrade(email, tradeDto);
        return responseService.getSuccessResult();
    }

    /**
     * 상품의 체결 거래 내역
     *
     * @param productId 상품 id
     * @param pageable  페이지 정보
     * @return 검색 결과
     */
    @GetMapping("/products/{productId}/done")
    public Result findDoneTrade(@PathVariable("productId") Long productId, Pageable pageable) {
        return responseService.getSingleResult(tradeService.findDoneTrade(productId, pageable));
    }

    /**
     * 상품의 입찰 내역
     *
     * @param productId  상품 id
     * @param tradeState 입찰 상태(판매, 구매)
     * @param pageable   페이지 정보
     * @return 검색 결과
     */
    @GetMapping("/products/{productId}/{tradeState}")
    public Result findTransactionTrade(@PathVariable("productId") Long productId, @PathVariable("tradeState") String tradeState, Pageable pageable) {
        return responseService.getSingleResult(tradeService.findTransactionTrade(productId, tradeState, pageable));
    }

    /**
     * 즉시 거래가
     * @param productId 상품 id
     * @param tradeState 입찰 상태(판매, 구매)
     * @return 검색 결과
     */
    @GetMapping("/products/{productId}/{tradeState}/instant")
    public Result findInstantTrade(@PathVariable("productId") Long productId, @PathVariable("tradeState") String tradeState) {
        return responseService.getSingleResult(tradeService.findInstantTrade(productId, tradeState));
    }
}
