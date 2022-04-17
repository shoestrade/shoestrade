package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.SalesTradeSaveDto;
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
     * 판매 입찰 등록
     * @param email 판매자 이메일
     * @param salesTradeSaveDto 판매 입찰 정보
     * @return 성공 결과
     */
    @PostMapping("/sales")
    public Result salesTradeSave(@LoginMember String email, @RequestBody SalesTradeSaveDto salesTradeSaveDto) {
        tradeService.salesTradeSave(email, salesTradeSaveDto);
        return responseService.getSuccessResult();
    }

    /**
     * 사용자가 등록한 입찰 내역 검색
     *
     * @param email 사용자 이메일
     * @param pageable 페이지
     * @return 검색 결과
     */
    @GetMapping("/sales")
    public Result salesTrade(@LoginMember String email, Pageable pageable) {
        return responseService.getSingleResult(tradeService.findTradeByEmailAndTradeType(email, TradeType.SELL, pageable));
    }
}
