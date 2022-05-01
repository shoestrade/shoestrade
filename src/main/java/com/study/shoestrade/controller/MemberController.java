package com.study.shoestrade.controller;

import com.study.shoestrade.common.annotation.LoginMember;
import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.account.AccountDto;
import com.study.shoestrade.dto.address.AddressDto;
import com.study.shoestrade.dto.address.response.AddressListResponseDto;
import com.study.shoestrade.dto.member.request.*;
import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.dto.member.response.MemberFindResponseDto;
import com.study.shoestrade.dto.member.response.MemberLoginResponseDto;
import com.study.shoestrade.dto.member.response.PointDto;
import com.study.shoestrade.dto.product.request.ProductSaveDto;
import com.study.shoestrade.service.member.MailService;
import com.study.shoestrade.service.member.LoginService;
import com.study.shoestrade.service.member.MemberService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final LoginService loginService;
    private final ResponseService responseService;
    private final MailService mailService;
    private final MemberService memberService;

    @ApiOperation(value = "회원가입", notes = "회원을 가입합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "회원가입 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberJoinDto", value = "회원 가입할 정보", dataTypeClass = MemberJoinDto.class)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<MemberDto> join(@RequestBody MemberJoinDto memberJoinDto) {
        MemberDto memberDto = loginService.joinMember(memberJoinDto);
        return responseService.getSingleResult(memberDto);
    }

    @ApiOperation(value = "인증번호 이메일 발송", notes = "회원 가입시 이메일 인증번호를 발송합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "인증번호 이메일 발송 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "회원 가입할 이메일", dataTypeClass = String.class)
    })
    @PostMapping("/join/send-mail")
    @ResponseStatus(HttpStatus.OK)
    public Result mail(@RequestParam("email") String email) {
        String key = mailService.sendMail(email);
        mailService.saveKey(email, key);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "인증번호 확인", notes = "회원 가입시 이메일로 전송된 인증번호를 검증합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "인증번호 확인 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "회원 가입할 이메일", dataTypeClass = String.class),
            @ApiImplicitParam(name = "key", value = "인증번호", dataTypeClass = String.class)
    })
    @PostMapping("/join/check-mail")
    @ResponseStatus(HttpStatus.OK)
    public Result mailCheck(@RequestParam("email") String email, @RequestParam("key") String key) {
        mailService.checkKey(email, key);
        // 삭제 추가
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "로그인", notes = "로그인합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requestDto", value = "로그인 아이디, 비밀번호", dataTypeClass = MemberLoginRequestDto.class),
    })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<MemberLoginResponseDto> login(@RequestBody MemberLoginRequestDto requestDto) {
        MemberLoginResponseDto responseDto = loginService.login(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "토큰 재발급", notes = "토큰을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "토큰 재발급 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requestDto", value = "사용 중인 JWT 토큰 정보", dataTypeClass = TokenRequestDto.class),
    })
    @PostMapping("/token/reissuance")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<MemberLoginResponseDto> reIssue(@RequestBody TokenRequestDto requestDto) {
        MemberLoginResponseDto responseDto = loginService.reIssue(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "로그아웃", notes = "로그아웃을 합니다. (중요!! 프론트에서 jwt accessToken을 지워야 한다.)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그아웃 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
    })
    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public Result logout(@LoginMember String email) {
        loginService.logout(email);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "이메일 찾기", notes = "휴대폰 번호로 이메일을 찾습니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "이메일 찾기 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requestDto", value = "이메일 찾을 때 필요한 정보(휴대폰 번호만)", dataTypeClass = MemberFindRequestDto.class)
    })
    @PostMapping("/find-email")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<MemberFindResponseDto> findEmail(@RequestBody MemberFindRequestDto requestDto) {
        MemberFindResponseDto responseDto = loginService.findEmail(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "비밀번호 찾기", notes = "휴대폰 번호와 이메일로 비밀번호를 찾습니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "비밀번호 찾기 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requestDto", value = "이메일 찾을 때 필요한 정보", dataTypeClass = MemberFindRequestDto.class)
    })
    @PostMapping("/find-password")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<MemberFindResponseDto> findPassword(@RequestBody MemberFindRequestDto requestDto) {
        MemberFindResponseDto responseDto = loginService.findPassword(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴를 합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 탈퇴 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "requestDto", value = "로그인 정보", dataTypeClass = MemberLoginRequestDto.class)
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public Result withdrawalMember(@LoginMember String email, @RequestBody MemberLoginRequestDto requestDto) {
        loginService.deleteMember(email, requestDto);
//        loginService.logout(email);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "주소 등록", notes = "회원을 주소(배송지)를 등록 합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "주소 등록 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "requestDto", value = "로그인 정보", dataTypeClass = AddressDto.class)
    })
    @PostMapping("/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public Result addAddress(@LoginMember String email, @RequestBody AddressDto requestDto) {
        memberService.addAddress(email, requestDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "주소 목록", notes = "회원의 주소목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "주소 목록 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
    })
    @GetMapping("/addresses")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<AddressListResponseDto> getAddressList(@LoginMember String email, @PageableDefault(size = 10) Pageable pageable) {
        AddressListResponseDto responseDto = memberService.getAddressList(email, pageable);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "기본 배송지 변경", notes = "회원의 배송지를 변경합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "기본 배송지 변경 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "addressId", value = "주소 id", dataTypeClass = Long.class),
    })
    @PostMapping("/addresses/base-address/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public Result changeBaseAddress(@LoginMember String email, @PathVariable("addressId") Long id) {
        memberService.changeBaseAddress(email, id);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "주소 수정", notes = "회원의 주소를 수정합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "주소 수정 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "addressId", value = "주소 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "requestDto", value = "수정할 주소 정보", dataTypeClass = AddressDto.class),
    })
    @PostMapping("/addresses/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public Result updateAddress(@LoginMember String email, @PathVariable("addressId") Long id, @RequestBody AddressDto requestDto) {
        memberService.updateAddress(email, id, requestDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "주소 삭제", notes = "회원의 주소를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "주소 삭제 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "삭제할 주소 id", dataTypeClass = Long.class),
    })
    @DeleteMapping("/addresses/{addressId}")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteAddress(@LoginMember String email, @PathVariable("addressId") Long id) {
        memberService.deleteAddress(email, id);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "등록 계좌 보기", notes = "회원의 등록된 계좌를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "등록 계좌 보기 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
    })
    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<AccountDto> getAccount(@LoginMember String email) {
        AccountDto responseDto = memberService.getAccount(email);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "계좌 등록 및 수정", notes = "회원의 계좌를 등록 및 수정합니다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "계좌 등록 및 수정 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "requestDto", value = "등록 및 수정할 계좌 정보", dataTypeClass = AccountDto.class),
    })
    @PostMapping("/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<AccountDto> addAccount(@LoginMember String email, @RequestBody AccountDto requestDto) {
        AccountDto responseDto = memberService.addAccount(email, requestDto);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "계좌 삭제", notes = "회원의 계좌를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "계좌 삭제 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
    })
    @DeleteMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteAccount(@LoginMember String email) {
        memberService.deleteAccount(email);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "포인트 보기", notes = "회원의 포인트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "포인트 보기 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
    })
    @GetMapping("/point")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<PointDto> getPoint(@LoginMember String email) {
        PointDto responseDto = memberService.getPoint(email);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "프로필 보기", notes = "회원의 프로필을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "프로필 보기 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<MemberDto> getProfile(@LoginMember String email) {
        MemberDto responseDto = memberService.getProfile(email);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "비밀번호 변경", notes = "회원의 비밀번호를 변경합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "비밀번호 변경 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "requestDto", value = "변경할 비밀번호 정보", dataTypeClass = PasswordDto.class),
    })
    @PostMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public Result changePassword(@LoginMember String email, @RequestBody PasswordDto requestDto) {
        memberService.changePassword(email, requestDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "휴대폰 번호 변경", notes = "회원의 휴대폰 번호를 변경합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "휴대폰 번호 변경 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "number", value = "변경할 휴대폰 번호", dataTypeClass = String.class),
    })
    @PostMapping("/phone")
    @ResponseStatus(HttpStatus.OK)
    public Result changePhone(@LoginMember String email, @RequestParam("number") String number) {
        memberService.changePhone(email, number);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "신발 사이즈 변경", notes = "회원의 신발 사이즈를 변경합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "신발 사이즈 변경 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "로그인된 사용자 이메일", dataTypeClass = String.class, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "변경할 신발 사이즈", dataTypeClass = String.class),
    })
    @PostMapping("/shoe-size")
    @ResponseStatus(HttpStatus.OK)
    public Result changeShoeSize(@LoginMember String email, @RequestParam("size") String size) {
        memberService.changeShoeSize(email, size);
        return responseService.getSuccessResult();
    }
}
