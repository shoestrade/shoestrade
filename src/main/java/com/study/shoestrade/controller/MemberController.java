package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.member.request.MemberFindRequestDto;
import com.study.shoestrade.dto.member.request.MemberJoinDto;
import com.study.shoestrade.dto.member.request.MemberLoginRequestDto;
import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.dto.member.response.MemberFindResponseDto;
import com.study.shoestrade.dto.member.response.MemberLoginResponseDto;
import com.study.shoestrade.service.MailService;
import com.study.shoestrade.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final ResponseService responseService;

    private final MailService mailService;

    @PostMapping("/join")
    public SingleResult<MemberDto> join(@RequestBody MemberJoinDto memberJoinDto){
        log.info("MemberController -> join");
        
        MemberDto memberDto = memberService.joinMember(memberJoinDto);
        return responseService.getSingleResult(memberDto);
    }

    // 인증번호 이메일 발송
    @PostMapping("/join/mail")
    public Result mail(@RequestParam("email") String email){
        String key = mailService.sendMail(email);
        mailService.saveKey(email, key);
        return responseService.getSuccessResult();
    }

    // 인증번호 확인
    @PostMapping("/join/mailCheck")
    public Result mailCheck(@RequestParam("email") String email, @RequestParam("key") String key){
        mailService.checkKey(email, key);
        // 삭제 추가
        return responseService.getSuccessResult();
    }

    // 로그인
    @PostMapping("/login")
    public SingleResult<MemberLoginResponseDto> login(@RequestBody MemberLoginRequestDto requestDto){
        log.info("login");
        MemberLoginResponseDto responseDto = memberService.login(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public Result logout(){
        log.info("logout");
        memberService.logout();
        return responseService.getSuccessResult();
    }

    // 이메일 찾기
    @PostMapping("login/find_email")
    public SingleResult<MemberFindResponseDto> findEmail(@RequestBody MemberFindRequestDto requestDto){
        log.info("MemberController -> findEmail 실행");
        MemberFindResponseDto responseDto = memberService.findEmail(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    // 비밀번호 찾기
    @PostMapping("/login/find_password")
    public SingleResult<MemberFindResponseDto> findPassword(@RequestBody MemberFindRequestDto requestDto){
        log.info("MemberController -> findPassword 실행");
        MemberFindResponseDto responseDto = memberService.findPassword(requestDto);
        return responseService.getSingleResult(responseDto);
    }
}
