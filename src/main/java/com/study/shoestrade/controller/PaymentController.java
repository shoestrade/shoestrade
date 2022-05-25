package com.study.shoestrade.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.payment.request.PaymentRequestDto;
import com.study.shoestrade.dto.payment.request.PaymentVerifyRequestDto;
import com.study.shoestrade.service.payment.PaymentService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final ResponseService responseService;
    private final PaymentService paymentService;

    @ApiOperation(value = "결제 정보 생성", notes = "결제 정보를 생성합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "결제 정보 생성 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "requestDto", value = "결제 정보 생성을 요청할 정보", dataTypeClass = PaymentRequestDto.class)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<String> createPayment(@LoginMember String email, @RequestBody PaymentRequestDto requestDto){
        String orderId = paymentService.createPayment(email, requestDto);
        return responseService.getSingleResult(orderId);
    }

    @ApiOperation(value = "결제 정보 검증", notes = "결제 정보를 검증합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "결제 정보 검증 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "requestDto", value = "결제 정보 검증을 요청할 정보", dataTypeClass = PaymentVerifyRequestDto.class)
    })
    @PostMapping("/verification")
    @ResponseStatus(HttpStatus.OK)
    public Result verifyPayment(@LoginMember String email, @RequestBody PaymentVerifyRequestDto requestDto) throws IamportResponseException, IOException {
        paymentService.verifyPayment(email, requestDto);
        return responseService.getSuccessResult();
    }
}
