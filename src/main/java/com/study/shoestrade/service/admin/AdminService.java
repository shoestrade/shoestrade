package com.study.shoestrade.service.admin;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.member.Role;
import com.study.shoestrade.dto.admin.PageMemberDto;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class  AdminService {

    private final MemberRepository memberRepository;

    // 회원 리스트 조회
    @Transactional(readOnly = true)
    public Page<PageMemberDto> getMembers(String email, Pageable pageable){
        return memberRepository.findMembers(email, pageable).map(PageMemberDto::create);
    }

    // 회원 상세 정보 조회


    // 회원 정지
    public void banMember(Long member_id, int day){
        Member findMember = memberRepository.findById(member_id).orElseThrow(MemberNotFoundException::new);
        LocalDateTime now = LocalDateTime.now();

        if(day == -1){
            memberRepository.delete(findMember);
        } else {
            findMember.changeRole(Role.BAN);
            findMember.updateBanReleaseTime(now.plusDays(day));
        }
    }

    // 회원 정지 해제
    public void releaseMember(Long member_id){
        Member findMember = memberRepository.findById(member_id).orElseThrow(MemberNotFoundException::new);

        findMember.changeRole(Role.ROLE_MEMBER);
        findMember.updateBanReleaseTime(LocalDateTime.now());
    }


}
