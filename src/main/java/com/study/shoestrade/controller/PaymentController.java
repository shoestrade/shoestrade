package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.member.request.MemberJoinDto;
import com.study.shoestrade.dto.payment.request.PaymentRequestDto;
import com.study.shoestrade.service.payment.PaymentService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    

//    // 결제 확인? 후 상태 변경
//    @PostMapping("/{tradeId}/payments/{paymentId}")
}
