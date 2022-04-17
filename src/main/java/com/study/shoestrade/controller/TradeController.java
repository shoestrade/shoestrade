package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.SalesTradeSaveDto;
import com.study.shoestrade.dto.trade.request.TradeDeleteDto;
import com.study.shoestrade.dto.trade.request.TradeUpdateDto;
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
     * @param email             사용자 이메일
     * @param salesTradeSaveDto 입찰 정보
     * @return 성공 결과
     */
    @PostMapping
    public Result salesTradeSave(@LoginMember String email, @RequestBody SalesTradeSaveDto salesTradeSaveDto) {
        tradeService.salesTradeSave(email, salesTradeSaveDto);
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
    @GetMapping
    public Result salesTrade(@LoginMember String email, @RequestParam TradeType tradeType, Pageable pageable) {
        return responseService.getSingleResult(tradeService.findTradeByEmailAndTradeType(email, tradeType, pageable));
    }

    /**
     * 입찰 금액 수정
     *
     * @param email          사용자 이메일
     * @param tradeUpdateDto 수정할 입찰 정보
     * @return 성공 결과
     */
    @PostMapping("/update")
    public Result updateTrade(@LoginMember String email, @RequestBody TradeUpdateDto tradeUpdateDto) {
        tradeService.updateTrade(email, tradeUpdateDto);
        return responseService.getSuccessResult();
    }

    /**
     * 입찰 삭제
     * @param email          사용자 이메일
     * @param tradeDeleteDto 삭제할 입찰 정보
     * @return  성공 결과
     */
    @DeleteMapping
    public Result deleteTrade(@LoginMember String email, @RequestBody TradeDeleteDto tradeDeleteDto) {
        tradeService.deleteTrade(email, tradeDeleteDto);
        return responseService.getSuccessResult();
    }
}
