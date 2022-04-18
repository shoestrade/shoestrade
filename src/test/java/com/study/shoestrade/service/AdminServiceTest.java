package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.admin.PageMemberDto;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.service.admin.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.study.shoestrade.domain.member.Role.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    AdminService adminService;
    @Mock
    MemberRepository memberRepository;

    List<Member> members;

    @BeforeEach
    public void init(){
        Member member1 = Member.builder()
                .id(1L)
                .email("aaa")
                .name("aaa")
                .phone("01011111111")
                .point(123)
                .shoeSize(250)
                .role(ROLE_MEMBER)
                .banReleaseTime(LocalDateTime.of(2022, 4, 18, 13, 13, 13))
                .build();

        Member member2 = Member.builder()
                .id(2L)
                .email("bbb")
                .name("bbb")
                .phone("01022222222")
                .point(29)
                .shoeSize(255)
                .role(ROLE_MEMBER)
                .banReleaseTime(LocalDateTime.now())
                .build();

        Member member3 = Member.builder()
                .id(3L)
                .email("ccc")
                .name("ccc")
                .phone("01033333333")
                .point(0)
                .shoeSize(260)
                .role(BAN)
                .banReleaseTime(LocalDateTime.now())
                .build();

        members = new ArrayList<>();
        members.add(member1);
        members.add(member2);
        members.add(member3);

    }

    @Test
    public void 회원_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 1);
        Page<Member> page = new PageImpl<>(members, pageable, 3L);

        // mocking
        given(memberRepository.findMembers(any(), any())).willReturn(page);

        // when
        Page<PageMemberDto> responseDto = adminService.getMembers("", pageable);

        // then
        assertThat(responseDto.getContent().size()).isEqualTo(3);
    }


    @Test
    @DisplayName("관리자가 기간을 설정하고 정지 요청하면 회원 정지에 성공한다.")
    public void 회원_정지_성공1() {
        // given
        Member member = members.get(1);
        LocalDateTime time = member.getBanReleaseTime();
        assertThat(member.getRole()).isEqualTo(ROLE_MEMBER);

        // mocking
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        // when
        adminService.banMember(1L, 3);

        // then
        assertThat(member.getRole()).isEqualTo(BAN);
        assertThat(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(member.getBanReleaseTime()))
                .isEqualTo(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(time.plusDays(3)));
    }

    @Test
    @DisplayName("정지 기간을 -1로 설정하면 회원을 탈퇴시킨다.")
    public void 회원_정지_성공2() {
        // given
        Member member = members.get(1);

        // mocking
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        // when, then
        assertThatCode(() ->  adminService.banMember(1L, -1))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("회원 정지 해제에 성공한다.")
    public void 회원_정지_해제_성공() {
        // given
        Member member = members.get(2);
        assertThat(member.getRole()).isEqualTo(BAN);

        // mocking
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        // when
        adminService.releaseMember(2L);

        // then
        assertThat(member.getRole()).isEqualTo(ROLE_MEMBER);
    }

}