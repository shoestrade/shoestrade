package com.study.shoestrade.exception;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.exception.address.AddressNotFoundException;
import com.study.shoestrade.exception.address.BaseAddressNotDeleteException;
import com.study.shoestrade.exception.address.BaseAddressUncheckedException;
import com.study.shoestrade.exception.brand.BrandDuplicationException;
import com.study.shoestrade.exception.brand.BrandEmptyResultDataAccessException;
import com.study.shoestrade.exception.interest.InterestNotFoundException;
import com.study.shoestrade.exception.mailAuth.MailAuthNotEqualException;
import com.study.shoestrade.exception.mailAuth.MailNotValidException;
import com.study.shoestrade.exception.member.*;
import com.study.shoestrade.exception.payment.*;
import com.study.shoestrade.exception.product.*;
import com.study.shoestrade.exception.token.ExpiredRefreshTokenException;
import com.study.shoestrade.exception.token.InvalidRefreshTokenException;
import com.study.shoestrade.exception.token.TokenNotFoundException;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.exception.trade.TradeNotCompletedException;
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

    @ExceptionHandler(InsufficientPointException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Result insufficientPointException(InsufficientPointException e){
        return responseService.getFailureResult(-122, "포인트가 부족합니다.");
    }

    @ExceptionHandler(MyTradeException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Result myTradeException(MyTradeException e){
        return responseService.getFailureResult(-123, "자신이 등록한 입찰은 거래할 수 없습니다.");
    }

    @ExceptionHandler(PaymentPriceNotMatchedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Result paymentNotMatchedException(PaymentPriceNotMatchedException e){
        return responseService.getFailureResult(-124, "가격이 일치하지 않습니다.");
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected Result paymentNotFoundException(PaymentNotFoundException e){
        return responseService.getFailureResult(-125, e.getMessage() + "인 결제 내역을 찾을 수 없습니다.");
    }

    @ExceptionHandler(IamportResponseException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected Result iamportResponseException(IamportResponseException e){
        return responseService.getFailureResult(-126, e.getMessage());
    }

    @ExceptionHandler(PaymentOrderIdNotConsistException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Result paymentOrderIdNotConsistException(PaymentOrderIdNotConsistException e){
        return responseService.getFailureResult(-127, "주문 번호가 일치하지 않습니다.");
    }

    @ExceptionHandler(PaymentUnpaidException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Result paymentUnpaidException(PaymentUnpaidException e){
        return responseService.getFailureResult(-128, "결제가 이루어지지 않았습니다.");
    }

    @ExceptionHandler(PaymentMethodNotConsistException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Result paymentMethodNotConsistException(PaymentMethodNotConsistException e){
        return responseService.getFailureResult(-129, "결제 수단이 일치하지 않습니다.");
    }

    @ExceptionHandler(TradeNotCompletedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Result tradeNotCompletedException(TradeNotCompletedException e){
        return responseService.getFailureResult(-130, "거래를 수정할 수 없는 단계입니다.");
    }

    @ExceptionHandler(MailNotValidException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Result mailNotValidException(MailNotValidException e){
        return responseService.getFailureResult(-131, "올바르지 못한 이메일 형식입니다.");
    }

    @ExceptionHandler(PaymentRestTemplateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Result paymentRestTemplateException(PaymentRestTemplateException e){
        return responseService.getFailureResult(-132, "api 호출 시 에러가 발생했습니다.");
    }

    @ExceptionHandler(PaymentCanceledException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    protected Result paymentCanceledException(PaymentCanceledException e){
        return responseService.getFailureResult(-133, "이미 전액 환불된 주문입니다.");
    }

    @ExceptionHandler(PaymentCancelFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected Result paymentCancelFailureException(PaymentCancelFailureException e){
        return responseService.getFailureResult(-134, "환불에 실패하였습니다.");
    }
}
