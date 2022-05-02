package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.dto.product.response.ProductLoadDto;
import com.study.shoestrade.dto.trade.request.TradeDto;
import com.study.shoestrade.dto.trade.response.TradeBreakdownCountDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import com.study.shoestrade.service.trade.TradeService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/trades")
@RequiredArgsConstructor
public class TradeController {

    private final ResponseService responseService;

    private final TradeService tradeService;

    @ApiOperation(value = "(구매, 판매)입찰 등록", notes = "입찰을 등록합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "입찰 등록 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "tradeDto", value = "등록할 입찰 정보", dataTypeClass = TradeDto.class),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result salesTradeSave(@LoginMember String email, @RequestBody TradeDto tradeDto) {
        tradeService.TradeSave(email, tradeDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "회원의 전체 거래 내역 수 조회", notes = "회원 본인이 등록한 전체 거래 내역 수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원의 전체 거래 내역 수 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "tradeType", value = "거래 형태(sell : 구매 거래 내역, purchase : 판매 거래 내역)", example = "'sell' or 'purchase'", dataTypeClass = String.class)
    })
    @GetMapping("/{tradeType}/count")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<TradeBreakdownCountDto> getBreakdownCount(@LoginMember String email, @PathVariable("tradeType") String tradeType){
        return responseService.getSingleResult(tradeService.getBreakdownCount(email, tradeType));
    }

    @ApiOperation(value = "회원 거래 내역 조회", notes = "회원 본인의 거래 내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 거래 내역 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "tradeType", value = "거래 형태('sell' or 'purchase')", example = "'sell' or 'purchase'", dataTypeClass = String.class),
            @ApiImplicitParam(name = "state", value = "거래 상태(bid : 입찰, progress : 진행 중, done : 종료(완료))", example = "'bid' or 'progress' or 'done'", dataTypeClass = String.class)
    })
    @GetMapping("/{tradeType}/{state}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<TradeLoadDto>> getBreakdown(@LoginMember String email, @PathVariable("tradeType") String tradeType, @PathVariable("state") String state, Pageable pageable){
        return responseService.getSingleResult(tradeService.getBreakdown(email, tradeType, state, pageable));
    }

    @ApiOperation(value = "입찰 금액 수정", notes = "회원이 등록한 입찰 금액을 수정합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "입찰 금액 수정 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "tradeId", value = "입찰 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "tradeDto", value = "수정할 입찰 정보(수정할 가격, 기존 입찰 타입만 입력)", dataTypeClass = TradeDto.class),
    })
    @PostMapping("/{tradeId}/price")
    @ResponseStatus(HttpStatus.OK)
    public Result updateTrade(@LoginMember String email, @PathVariable("tradeId") Long tradeId, @RequestBody TradeDto tradeDto) {
        tradeService.updateTrade(email, tradeId, tradeDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "입찰 삭제", notes = "회원이 등록한 입찰을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "입찰 삭제 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "tradeId", value = "입찰 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "tradeDto", value = "삭제할 입찰 정보(기존 입찰 타입만 입력)", dataTypeClass = TradeDto.class),
    })
    @DeleteMapping("/{tradeId}")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteTrade(@LoginMember String email, @PathVariable("tradeId") Long tradeId, @RequestBody TradeDto tradeDto) {
        tradeService.deleteTrade(email, tradeDto);
        return responseService.getSuccessResult();
    }
}
