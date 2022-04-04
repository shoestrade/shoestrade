package com.study.shoestrade.exception;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.exception.mailAuth.MailAuthNotEqualException;
import com.study.shoestrade.exception.member.MemberDuplicationEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
