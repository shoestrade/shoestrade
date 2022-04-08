package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.ListResult;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.address.AddressDto;
import com.study.shoestrade.dto.member.request.MemberFindRequestDto;
import com.study.shoestrade.dto.member.request.MemberJoinDto;
import com.study.shoestrade.dto.member.request.MemberLoginRequestDto;
import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.dto.member.response.MemberFindResponseDto;
import com.study.shoestrade.dto.member.response.MemberLoginResponseDto;
import com.study.shoestrade.service.MailService;
import com.study.shoestrade.service.LoginService;
import com.study.shoestrade.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final LoginService loginService;
    private final ResponseService responseService;
    private final MailService mailService;
    private final MemberService memberService;

    @PostMapping("/join")
    public SingleResult<MemberDto> join(@RequestBody MemberJoinDto memberJoinDto){
        log.info("MemberController -> join");
        
        MemberDto memberDto = loginService.joinMember(memberJoinDto);
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
        MemberLoginResponseDto responseDto = loginService.login(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public Result logout(){
        log.info("logout");
        loginService.logout();
        return responseService.getSuccessResult();
    }

    // 이메일 찾기
    @PostMapping("login/find_email")
    public SingleResult<MemberFindResponseDto> findEmail(@RequestBody MemberFindRequestDto requestDto){
        log.info("MemberController -> findEmail 실행");
        MemberFindResponseDto responseDto = loginService.findEmail(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    // 비밀번호 찾기
    @PostMapping("/login/find_password")
    public SingleResult<MemberFindResponseDto> findPassword(@RequestBody MemberFindRequestDto requestDto){
        log.info("MemberController -> findPassword 실행");
        MemberFindResponseDto responseDto = loginService.findPassword(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    // 회원 탈퇴
    @DeleteMapping("/my/withdrawal")
    public SingleResult<MemberLoginResponseDto> withdrawalMember(@RequestBody MemberLoginRequestDto requestDto){
        log.info("MemberController -> withdrawalMember 실행");

        MemberLoginResponseDto responseDto = loginService.deleteMember(requestDto);
        loginService.logout();
        return responseService.getSingleResult(responseDto);
    }

    // 주소 등록
    @PostMapping("/my/address")
    public Result addAddress(@LoginMember String email, @RequestBody AddressDto requestDto){
        log.info("MemberController -> addAddress 실행");

        memberService.addAddress(email, requestDto);
        return responseService.getSuccessResult();
    }

    // 주소 목록
    @GetMapping("/my/address")
    public ListResult<AddressDto> getAddressList(@LoginMember String email){
        log.info("MemberController -> getAddressList 실행");

        List<AddressDto> addressList = memberService.getAddressList(email);
        return responseService.getListResult(addressList);
    }
}
