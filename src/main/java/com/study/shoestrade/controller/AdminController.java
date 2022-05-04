package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.address.response.AddressListResponseDto;
import com.study.shoestrade.dto.interest.response.MyInterest;
import com.study.shoestrade.dto.member.response.MemberDetailDto;
import com.study.shoestrade.dto.admin.PageMemberDto;
import com.study.shoestrade.dto.trade.response.TradeBreakdownCountDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import com.study.shoestrade.service.admin.AdminService;
import com.study.shoestrade.service.interest.InterestService;
import com.study.shoestrade.service.member.MemberService;
import com.study.shoestrade.service.trade.TradeService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final InterestService interestService;
    private final TradeService tradeService;
    private final ResponseService responseService;

    @ApiOperation(value = "회원 리스트 조회", notes = "회원의 전체 목록이 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 리스트 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "검색할 회원 이메일", example = "cjswltjr159@naver.com", dataTypeClass = String.class)
    })
    @GetMapping("/admin/members")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<PageMemberDto>> getMembers(@RequestParam(value = "email", required = false) @Nullable String email,
                                                        @PageableDefault(size = 20) Pageable pageable) {
        Page<PageMemberDto> responseDto = adminService.getMembers(email, pageable);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "회원 상세 정보 조회", notes = "회원에 대한 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 상세 정보 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "검색할 회원 id", dataTypeClass = Long.class)
    })
    @GetMapping("/admin/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<MemberDetailDto> getMemberDetail(@PathVariable("id") Long id) {
        MemberDetailDto responseDto = adminService.getMemberDetail(id);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "회원 주소 정보 조회", notes = "회원에 대한 주소 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 주소 정보 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "검색할 회원 id", dataTypeClass = Long.class)
    })
    @GetMapping("/admin/members/{id}/addresses")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<AddressListResponseDto> getMemberAddressList(@PathVariable("id") Long id, @PageableDefault(size = 10) Pageable pageable) {
        String email = adminService.getMemberEmail(id);
        AddressListResponseDto responseDto = memberService.getAddressList(email, pageable);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "회원 관심 상품 목록 조회", notes = "회원에 대한 관심 상품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 관심 상품 목록 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "검색할 회원 id", dataTypeClass = Long.class)
    })
    @GetMapping("/admin/members/{id}/wishes")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<MyInterest>> getMemberWishList(@PathVariable("id") Long id, @PageableDefault(size = 10) Pageable pageable) {
        String email = adminService.getMemberEmail(id);
        Page<MyInterest> responseDto = interestService.getMyWishList(email, pageable);
        return responseService.getSingleResult(responseDto);
    }

    @ApiOperation(value = "회원 전체 거래 내역 수 조회", notes = "회원에 대한 전체 거래 내역 수를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 전체 거래 내역 수 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "검색할 회원 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "tradeType", value = "거래 형태(sell : 구매 거래 내역, purchase : 판매 거래 내역)", example = "'sell' or 'purchase'", dataTypeClass = String.class)
    })
    @GetMapping("/admin/members/{id}/trades/{tradeType}/count")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<TradeBreakdownCountDto> getMemberBreakdownCount(@PathVariable("id") Long id, @PathVariable("tradeType") String tradeType) {
        String email = adminService.getMemberEmail(id);
        return responseService.getSingleResult(tradeService.getBreakdownCount(email, tradeType));
    }

    @ApiOperation(value = "회원 거래 내역 조회", notes = "회원에 대한 거래 내역을 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 거래 내역 조회 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "검색할 회원 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "tradeType", value = "거래 형태('sell' or 'purchase')", example = "'sell' or 'purchase'", dataTypeClass = String.class),
            @ApiImplicitParam(name = "state", value = "거래 상태(bid : 입찰, progress : 진행 중, done : 종료(완료))", example = "'bid' or 'progress' or 'done'", dataTypeClass = String.class)
    })
    @GetMapping("/admin/members/{id}/trades/{tradeType}/{state}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<TradeLoadDto>> getMemberBreakdown(@PathVariable("id") Long id, @PathVariable("tradeType") String tradeType,
                                                               @PathVariable("state") String state, Pageable pageable) {
        String email = adminService.getMemberEmail(id);
        return responseService.getSingleResult(tradeService.getBreakdown(email, tradeType, state, pageable));
    }

    @ApiOperation(value = "회원 정지", notes = "회원을 정지시킵니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 정지 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "정지할 회원 id", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "day", value = "정지할 기간(일, -1이면 회원 탈퇴)", dataTypeClass = Integer.class)
    })
    @PostMapping("/admin/members/{id}/ban")
    @ResponseStatus(HttpStatus.OK)
    public Result banMember(@PathVariable("id") Long id, @RequestParam("day") int day) {
        adminService.banMember(id, day);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value = "회원 정지 해제", notes = "회원의 정지를 해제시킵니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 정지 해제 정상 처리")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "정지 해제할 회원 id", dataTypeClass = Long.class)
    })
    @PostMapping("/admin/members/{id}/release")
    @ResponseStatus(HttpStatus.OK)
    public Result releaseMember(@PathVariable("id") Long id) {
        adminService.releaseMember(id);
        return responseService.getSuccessResult();
    }
}
