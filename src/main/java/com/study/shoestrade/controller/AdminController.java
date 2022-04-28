package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.address.response.AddressListResponseDto;
import com.study.shoestrade.dto.interest.response.MyInterest;
import com.study.shoestrade.dto.member.response.MemberDetailDto;
import com.study.shoestrade.dto.admin.PageMemberDto;
import com.study.shoestrade.service.admin.AdminService;
import com.study.shoestrade.service.interest.InterestService;
import com.study.shoestrade.service.member.MemberService;
import com.study.shoestrade.service.trade.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final InterestService interestService;
    private final TradeService tradeService;
    private final ResponseService responseService;


    /**
     * 회원 리스트 조회
     *
     * @param email    : 검색할 회원 email
     * @param pageable : 페이징
     * @return
     */
    @GetMapping("/admin/members")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<PageMemberDto>> getMembers(@RequestParam("email") @Nullable String email,
                                                        @PageableDefault(size = 20) Pageable pageable) {
        Page<PageMemberDto> responseDto = adminService.getMembers(email, pageable);
        return responseService.getSingleResult(responseDto);
    }

    /**
     * 회원 상세 정보 조회
     *
     * @param id : 회원 id
     * @return
     */
    @GetMapping("/admin/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<MemberDetailDto> getMemberDetail(@PathVariable("id") Long id) {
        MemberDetailDto responseDto = adminService.getMemberDetail(id);
        return responseService.getSingleResult(responseDto);
    }

    /**
     * 회원 주소 정보 조회
     *
     * @param id       : 회원 id
     * @param pageable : 페이징
     * @return
     */
    @GetMapping("/admin/members/{id}/addresses")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<AddressListResponseDto> getMemberAddressList(@PathVariable("id") Long id, @PageableDefault(size = 10) Pageable pageable) {
        String email = adminService.getMemberEmail(id);
        AddressListResponseDto responseDto = memberService.getAddressList(email, pageable);
        return responseService.getSingleResult(responseDto);
    }

    /**
     * 회원 관심 상품 목록 조회
     *
     * @param id       : 회원 id
     * @param pageable : 페이징
     * @return
     */
    @GetMapping("/admin/members/{id}/wishes")
    @ResponseStatus(HttpStatus.OK)
    public SingleResult<Page<MyInterest>> getMemberWishList(@PathVariable("id") Long id, @PageableDefault(size = 10) Pageable pageable) {
        String email = adminService.getMemberEmail(id);
        Page<MyInterest> responseDto = interestService.getMyWishList(email, pageable);
        return responseService.getSingleResult(responseDto);
    }

    /**
     * 회원 거래 내역 수 전체 조회
     *
     * @param id        : 회원 id
     * @param tradeType : 거래 타입 {sell : 구매 거래 내역, purchase : 판매 거래 내역}
     * @return
     */
    @GetMapping("/admin/members/{id}/trades/{tradeType}/count")
    @ResponseStatus(HttpStatus.OK)
    public Result getMemberBreakdownCount(@PathVariable("id") Long id, @PathVariable("tradeType") String tradeType) {
        String email = adminService.getMemberEmail(id);
        return responseService.getSingleResult(tradeService.getBreakdownCount(email, tradeType));
    }

    /**
     * 회원 거래 내역 조회
     *
     * @param id        : 회원 id
     * @param tradeType : 거래 타입 {sell : 구매 거래 내역, purchase : 판매 거래 내역}
     * @param state     : 거래 상태 {bid : 입찰, progress : 진행 중, done : 종료(완료)}
     * @param pageable  : 페이징
     * @return
     */
    @GetMapping("/admin/members/{id}/trades/{tradeType}/{state}")
    @ResponseStatus(HttpStatus.OK)
    public Result getMemberBreakdown(@PathVariable("id") Long id, @PathVariable("tradeType") String tradeType,
                                     @PathVariable("state") String state, Pageable pageable) {
        String email = adminService.getMemberEmail(id);
        return responseService.getSingleResult(tradeService.getBreakdown(email, tradeType, state, pageable));
    }

    /**
     * 회원 정지
     *
     * @param id  : 정지할 회원 id
     * @param day : 정지할 기간(day)
     * @return
     */
    @PostMapping("/admin/members/{id}/ban")
    @ResponseStatus(HttpStatus.OK)
    public Result banMember(@PathVariable("id") Long id, @RequestParam("day") int day) {
        adminService.banMember(id, day);
        return responseService.getSuccessResult();
    }

    /**
     * 회원 정지 해제
     *
     * @param id : 정지 해제할 회원 id
     * @return
     */
    @PostMapping("/admin/members/{id}/release")
    @ResponseStatus(HttpStatus.OK)
    public Result releaseMember(@PathVariable("id") Long id) {
        adminService.releaseMember(id);
        return responseService.getSuccessResult();
    }
}
