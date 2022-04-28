package com.study.shoestrade.exception;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.exception.address.AddressNotFoundException;
import com.study.shoestrade.exception.address.BaseAddressNotDeleteException;
import com.study.shoestrade.exception.address.BaseAddressUncheckedException;
import com.study.shoestrade.exception.brand.BrandDuplicationException;
import com.study.shoestrade.exception.brand.BrandEmptyResultDataAccessException;
import com.study.shoestrade.exception.interest.InterestNotFoundException;
import com.study.shoestrade.exception.mailAuth.MailAuthNotEqualException;
import com.study.shoestrade.exception.member.*;
import com.study.shoestrade.exception.product.*;
import com.study.shoestrade.exception.token.ExpiredRefreshTokenException;
import com.study.shoestrade.exception.token.InvalidRefreshTokenException;
import com.study.shoestrade.exception.token.TokenNotFoundException;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.exception.trade.WrongStateException;
import com.study.shoestrade.exception.trade.WrongTradeTypeException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(MemberDuplicationEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result memberDuplicationEmailException(){
        return responseService.getFailureResult(-100, "이미 회원가입된 이메일 입니다.");
    }

    @ExceptionHandler(MailAuthNotEqualException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result mailAuthNotEqualException(){
        return responseService.getFailureResult(-101, "인증번호가 틀렸습니다.");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result memberNotFoundException(){
        return responseService.getFailureResult(-102, "존재하지 않은 회원입니다.");
    }

    @ExceptionHandler(WrongEmailException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result wrongEmailException(){
        return responseService.getFailureResult(-103, "아이디가 틀렸습니다.");
    }

    @ExceptionHandler(WrongPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result wrongPasswordException(){
        return responseService.getFailureResult(-104, "비밀번호가 틀렸습니다.");
    }

    @ExceptionHandler(AddressNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result addressNotFoundException(){
        return responseService.getFailureResult(-105, "존재하지 않는 주소입니다.");
    }

    @ExceptionHandler(BaseAddressNotDeleteException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result baseAddressNotDeleteException(){
        return responseService.getFailureResult(-106, "기본 주소는 삭제할 수 없습니다.");
    }

    @ExceptionHandler(BaseAddressUncheckedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result baseAddressUncheckedException(){
        return responseService.getFailureResult(-106, "기본 주소는 해제할 수 없습니다.");
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result invalidRefreshTokenException(){
        return responseService.getFailureResult(-107, "refreshToken이 일치하지 않습니다.");
    }

    @ExceptionHandler(ExpiredRefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result expiredRefreshTokenException(){
        return responseService.getFailureResult(-108, "refreshToken이 만료되었습니다.");
    }

    @ExceptionHandler(TokenNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result tokenNotFoundException(){
        return responseService.getFailureResult(-1000, "토큰이 존재하지 않습니다.");
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result signatureException(){
        return responseService.getFailureResult(-1001, "변조된 토큰입니다.");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result expiredJwtException(){
        return responseService.getFailureResult(-1002, "만료된 토큰입니다.");
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result malformedJwtException(){
        return responseService.getFailureResult(-1001, "변조된 토큰입니다.");
    }

    /**
     * 신발 사이즈 변경 시 String이 숫자이어야 함.
     */
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result numberFormatException(){
        return responseService.getFailureResult(-109, "입력값이 int형이 아닙니다.");
    }

    @ExceptionHandler(BanMemberException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result banMemberException(BanMemberException e){
        return responseService.getFailureResult(-110,  e.getMessage() +  "까지 정지된 회원입니다.");
    }

    @ExceptionHandler(InterestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result interestNotFoundException(InterestNotFoundException e){
        return responseService.getFailureResult(-111, "존재하지 않는 관심 상품입니다.");
    }

    @ExceptionHandler(WrongStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result wrongStateException(WrongStateException e){
        return responseService.getFailureResult(-112, e.getMessage() + "는 잘못된 내역조회 상태입니다.");
    }

    @ExceptionHandler(WrongTradeTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result wrongTradeTypeException(WrongTradeTypeException e){
        return responseService.getFailureResult(-113, e.getMessage() + "는 잘못된 거래 타입입니다.");
    }

    @ExceptionHandler(BrandDuplicationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected Result brandDuplicationException(BrandDuplicationException e) {
        return responseService.getFailureResult(-114, e.getMessage() + " : 이미 존재하는 브랜드 이름입니다.");
    }

    @ExceptionHandler(BrandEmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Result brandEmptyResultDataAccessException(BrandEmptyResultDataAccessException e) {
        return responseService.getFailureResult(-115, e.getMessage() + " : 해당 id의 브랜드를 찾을 수 없습니다.");
    }

    @ExceptionHandler(ProductDuplicationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected Result productDuplicationException(ProductDuplicationException e) {
        return responseService.getFailureResult(-116, e.getMessage() + " : 이미 존재하는 상품 이름입니다.");
    }

    @ExceptionHandler(ProductEmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Result productEmptyResultDataAccessException(ProductEmptyResultDataAccessException e) {
        return responseService.getFailureResult(-117, e.getMessage() + " : 해당 id의 상품을 찾을 수 없습니다.");
    }

    @ExceptionHandler(ProductImageDuplicationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected Result productImageDuplicationException(ProductImageDuplicationException e) {
        return responseService.getFailureResult(-118, "이미지 이름 (" + e.getMessage() + ") 이 중복됩니다.");
    }

    @ExceptionHandler(ProductImageEmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Result productImageEmptyResultDataAccessException(ProductImageEmptyResultDataAccessException e) {
        return responseService.getFailureResult(-119, e.getMessage() + " : 해당 id의 이미지를 찾을 수 없습니다.");
    }

    @ExceptionHandler(ProductSizeNoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Result productSizeNoSuchElementException(ProductSizeNoSuchElementException e) {
        return responseService.getFailureResult(-120, e.getMessage() + " : 해당 id의 신발사이즈를 찾을 수 없습니다.");
    }

    @ExceptionHandler(TradeEmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Result tradeEmptyResultDataAccessException(TradeEmptyResultDataAccessException e) {
        return responseService.getFailureResult(-121, e.getMessage() + " : 해당 입찰 내역을 찾을 수 없습니다.");
    }
}
