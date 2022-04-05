package com.study.shoestrade.common.advice;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.exception.BrandDuplicationException;
import com.study.shoestrade.exception.BrandEmptyResultDataAccessException;
import com.study.shoestrade.exception.ProductDuplicationException;
import com.study.shoestrade.exception.ProductEmptyResultDataAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

//    @ExceptionHandler(Exception.class)
//    protected Result defultException() {
//        return responseService.getFailureResult(-1, "에러 발생");
//    }

    @ExceptionHandler(BrandDuplicationException.class)
    protected Result brandDuplicationException() {
        return responseService.getFailureResult(-1, "이미 존재하는 브랜드 이름입니다.");
    }


    @ExceptionHandler(BrandEmptyResultDataAccessException.class)
    protected Result brandEmptyResultDataAccessException() {
        return responseService.getFailureResult(-1, "브랜드를 찾을 수 없습니다.");
    }

    @ExceptionHandler(ProductDuplicationException.class)
    protected Result productDuplicationException() {
        return responseService.getFailureResult(-1, "이미 존재하는 상품 이름입니다.");
    }

    @ExceptionHandler(ProductEmptyResultDataAccessException.class)
    protected Result productEmptyResultDataAccessException() {
        return responseService.getFailureResult(-1, "상품을 찾을 수 없습니다.");
    }
}
