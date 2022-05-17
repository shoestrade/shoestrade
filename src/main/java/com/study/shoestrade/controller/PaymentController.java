package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.payment.request.PaymentRequestDto;
import com.study.shoestrade.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final ResponseService responseService;
    private final PaymentService paymentService;

    // 결제 정보 생성
    @PostMapping
    public SingleResult<String> createPayment(@LoginMember String email, @RequestBody PaymentRequestDto requestDto){
        String orderId = paymentService.createPayment(email, requestDto);
        return responseService.getSingleResult(orderId);
    }

//    // 결제 확인? 후 상태 변경
//    @PostMapping("/{tradeId}/payments/{paymentId}")
}
