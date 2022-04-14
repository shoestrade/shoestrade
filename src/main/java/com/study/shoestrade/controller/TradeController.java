package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.dto.trade.request.SalesTradeSaveDto;
import com.study.shoestrade.service.trade.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
