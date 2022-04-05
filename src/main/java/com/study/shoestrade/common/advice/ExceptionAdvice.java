package com.study.shoestrade.common.advice;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
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
        log.info("info = {}", "Exception - BrandDuplicationException 발생");
        return responseService.getFailureResult(-1, "이미 존재하는 브랜드 이름입니다.");
    }

    @ExceptionHandler(BrandEmptyResultDataAccessException.class)
    protected Result brandEmptyResultDataAccessException() {
        log.info("info = {}", "Exception - BrandEmptyResultDataAccessException 발생");
        return responseService.getFailureResult(-1, "해당되는 브랜드를 찾을 수 없습니다.");
    }

    @ExceptionHandler(ProductDuplicationException.class)
    protected Result productDuplicationException() {
        log.info("info = {}", "Exception - ProductDuplicationException 발생");
        return responseService.getFailureResult(-1, "이미 존재하는 상품 이름입니다.");
    }

    @ExceptionHandler(ProductEmptyResultDataAccessException.class)
    protected Result productEmptyResultDataAccessException() {
        log.info("info = {}", "Exception - ProductEmptyResultDataAccessException 발생");
        return responseService.getFailureResult(-1, "해당되는 상품을 찾을 수 없습니다.");
    }
}
