package com.study.shoestrade.exception;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.exception.address.AddressNotFoundException;
import com.study.shoestrade.exception.address.BaseAddressNotDeleteException;
import com.study.shoestrade.exception.address.BaseAddressUncheckedException;
import com.study.shoestrade.exception.brand.BrandDuplicationException;
import com.study.shoestrade.exception.brand.BrandEmptyResultDataAccessException;
import com.study.shoestrade.exception.mailAuth.MailAuthNotEqualException;
import com.study.shoestrade.exception.member.WrongEmailException;
import com.study.shoestrade.exception.member.WrongPasswordException;
import com.study.shoestrade.exception.member.MemberDuplicationEmailException;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.product.*;
import com.study.shoestrade.exception.token.ExpiredRefreshTokenException;
import com.study.shoestrade.exception.token.InvalidRefreshTokenException;
import com.study.shoestrade.exception.token.TokenNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(MemberDuplicationEmailException.class)
    public Result memberDuplicationEmailException(){
        return responseService.getFailureResult(-100, "이미 회원가입된 이메일 입니다.");
    }

    @ExceptionHandler(MailAuthNotEqualException.class)
    public Result mailAuthNotEqualException(){
        return responseService.getFailureResult(-101, "인증번호가 틀렸습니다.");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public Result memberNotFoundException(){
        return responseService.getFailureResult(-102, "존재하지 않은 회원입니다.");
    }

    @ExceptionHandler(WrongEmailException.class)
    public Result wrongEmailException(){
        return responseService.getFailureResult(-103, "아이디가 틀렸습니다.");
    }

    @ExceptionHandler(WrongPasswordException.class)
    public Result wrongPasswordException(){
        return responseService.getFailureResult(-104, "비밀번호가 틀렸습니다.");
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public Result addressNotFoundException(){
        return responseService.getFailureResult(-105, "존재하지 않는 주소입니다.");
    }

    @ExceptionHandler(BaseAddressNotDeleteException.class)
    public Result baseAddressNotDeleteException(){
        return responseService.getFailureResult(-106, "기본 주소는 삭제할 수 없습니다.");
    }

    @ExceptionHandler(BaseAddressUncheckedException.class)
    public Result baseAddressUncheckedException(){
        return responseService.getFailureResult(-106, "기본 주소는 해제할 수 없습니다.");
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public Result invalidRefreshTokenException(){
        return responseService.getFailureResult(-107, "refreshToken이 일치하지 않습니다.");
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public Result expiredRefreshTokenException(){
        return responseService.getFailureResult(-108, "refreshToken이 만료되었습니다.");
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public Result tokenNotFoundException(){
        return responseService.getFailureResult(1000, "토큰이 존재하지 않습니다.");
    }

    @ExceptionHandler(SignatureException.class)
    public Result signatureException(){
        log.info("ExceptionAdvice -> signatureException 발생");
        return responseService.getFailureResult(1001, "변조된 토큰입니다.");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public Result expiredJwtException(){
        return responseService.getFailureResult(1002, "만료된 토큰입니다.");
    }

    @ExceptionHandler(MalformedJwtException.class)
    public Result malformedJwtException(){
        log.info("ExceptionAdvice -> malformedJwtException 발생");
        return responseService.getFailureResult(1001, "변조된 토큰입니다.");
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------- //

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
    protected Result productEmptyResultDataAccessException(ProductEmptyResultDataAccessException e) {
        log.info("info = {}", "Exception - ProductEmptyResultDataAccessException 발생");
        return responseService.getFailureResult(-1, e.getMessage() + " : 해당 id의 상품을 찾을 수 없습니다.");
    }

    @ExceptionHandler(ProductImageDuplicationException.class)
    protected Result productImageDuplicationException(ProductImageDuplicationException e) {
        log.info("info = {}", "Exception - ProductImageDuplicationException 발생");
        return responseService.getFailureResult(-1, "이미지 이름 (" + e.getMessage() + ") 이 중복됩니다.");
    }

    @ExceptionHandler(ProductImageEmptyResultDataAccessException.class)
    protected Result productImageEmptyResultDataAccessException(ProductImageEmptyResultDataAccessException e) {
        log.info("info = {}", "Exception - ProductImageEmptyResultDataAccessException 발생");
        return responseService.getFailureResult(-1, e.getMessage() + " : 해당 id의 이미지를 찾을 수 없습니다.");
    }

    @ExceptionHandler(ProductSizeNoSuchElementException.class)
    protected Result productSizeNoSuchElementException(ProductSizeNoSuchElementException e) {
        return responseService.getFailureResult(-1, e.getMessage() + " : 해당 id의 신발사이즈를 찾을 수 없습니다.");
    }
}
