package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.TradeDto;
import com.study.shoestrade.service.trade.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/trade")
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
     * 사용자가 등록한 입찰 내역 검색
     *
     * @param email     사용자 이메일
     * @param tradeType 입찰 타입
     * @param pageable  페이지
     * @return 검색 결과
     */
    @GetMapping("{tradeType}")
    public Result salesTrade(@LoginMember String email, @PathVariable TradeType tradeType, Pageable pageable) {
        return responseService.getSingleResult(tradeService.findTradeByEmailAndTradeType(email, tradeType, pageable));
    }

    /**
     * 입찰 금액 수정
     *
     * @param email    사용자 이메일
     * @param tradeDto 수정할 입찰 정보
     * @return 성공 결과
     */
    @PostMapping("/{id}")
    public Result updateTrade(@LoginMember String email, @PathVariable Long id, @RequestBody TradeDto tradeDto) {
        tradeService.updateTrade(email, id, tradeDto);
        return responseService.getSuccessResult();
    }

    /**
     * 입찰 삭제
     *
     * @param email    사용자 이메일
     * @param tradeDto 삭제할 입찰 정보
     * @return 성공 결과
     */
    @DeleteMapping
    public Result deleteTrade(@LoginMember String email, @RequestBody TradeDto tradeDto) {
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
    @GetMapping("/{productId}/done")
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
    @GetMapping("/{productId}/{tradeState}")
    public Result findTransactionTrade(@PathVariable("productId") Long productId, @PathVariable("tradeState") TradeState tradeState, Pageable pageable) {
        return responseService.getSingleResult(tradeService.findTransactionTrade(productId, tradeState, pageable));
    }
}
