package com.study.shoestrade.controller;

import com.study.shoestrade.common.response.ResponseService;
import com.study.shoestrade.common.result.Result;
import com.study.shoestrade.common.result.SingleResult;
import com.study.shoestrade.dto.admin.response.PageMemberDto;
import com.study.shoestrade.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;
    private final ResponseService responseService;

    /**
     * 회원 리스트 조회
     * @param email : 검색할 회원 email
     * @param pageable : 페이징
     * @return
     */
    @GetMapping("/admin/members")
    public SingleResult<Page<PageMemberDto>> getMembers(@RequestParam("email") @Nullable String email,
                                                  @PageableDefault(size = 20) Pageable pageable){
        Page<PageMemberDto> responseDto = adminService.getMembers(email, pageable);
        return responseService.getSingleResult(responseDto);
    }

//    @GetMapping("/admin/members/{id}")
//    public SingleResult<>

    /**
     * 회원 정지
     * @param id : 정지할 회원 id
     * @param day : 정지할 기간(day)
     * @return
     */
    @PostMapping("/admin/members/{id}/ban")
    public Result banMember(@PathVariable("id") Long id, @RequestParam("day") int day){
        adminService.banMember(id, day);
        return responseService.getSuccessResult();
    }

    /**
     * 회원 정지 해제
     * @param id : 정지 해제할 회원 id
     * @return
     */
    @PostMapping("/admin/members/{id}/release")
    public Result releaseMember(@PathVariable("id") Long id){
        adminService.releaseMember(id);
        return responseService.getSuccessResult();
    }
}
