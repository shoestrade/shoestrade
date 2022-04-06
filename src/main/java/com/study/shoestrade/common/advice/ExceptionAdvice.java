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
    protected Result brandDuplicationException(BrandDuplicationException e) {
        log.info("info = {}", "Exception - BrandDuplicationException 발생");
        return responseService.getFailureResult(-1, e.getMessage() + " : 이미 존재하는 브랜드 이름입니다.");
    }

    @ExceptionHandler(BrandEmptyResultDataAccessException.class)
    protected Result brandEmptyResultDataAccessException(BrandEmptyResultDataAccessException e) {
        log.info("info = {}", "Exception - BrandEmptyResultDataAccessException 발생");
        return responseService.getFailureResult(-1, e.getMessage() + " : 해당 id의 브랜드를 찾을 수 없습니다.");
    }

    @ExceptionHandler(ProductDuplicationException.class)
    protected Result productDuplicationException(ProductDuplicationException e) {
        log.info("info = {}", "Exception - ProductDuplicationException 발생");
        return responseService.getFailureResult(-1, e.getMessage() + " : 이미 존재하는 상품 이름입니다.");
    }

    @ExceptionHandler(ProductEmptyResultDataAccessException.class)
    protected Result productEmptyResultDataAccessException(ProductDuplicationException e) {
        log.info("info = {}", "Exception - ProductEmptyResultDataAccessException 발생");
        return responseService.getFailureResult(-1, e.getMessage() + " : 해당 id의 상품을 찾을 수 없습니다.");
    }

    @ExceptionHandler(ProductImageDuplicationException.class)
    protected Result productImageDuplicationException(ProductImageDuplicationException e) {
        log.info("info = {}", "Exception - ProductImageDuplicationException 발생");
        return responseService.getFailureResult(-1, "이미지 이름 (" + e.getMessage() + ") 이 중복됩니다.");
    }
}
