package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.member.request.MemberJoinDto;
import com.study.shoestrade.dto.member.request.MemberLoginRequestDto;
import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.exception.member.LoginFailureException;
import com.study.shoestrade.exception.member.MemberDuplicationEmailException;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    HttpSession session;

    @Test
    public void 회원가입_성공() {
        // given
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .email("tkk@gamil.com")
                .password("PW")
                .name("syt")
                .phone("01011112222")
                .shoeSize(255)
                .build();

        Member joinMember = memberJoinDto.toEntity("encode PW");
        MemberDto ExpectResult = MemberDto.create(joinMember);

        // mocking
        given(passwordEncoder.encode(any())).willReturn("encode PW");
        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(memberRepository.save(any())).willReturn(joinMember);

        // when
        MemberDto ActualResult = memberService.joinMember(memberJoinDto);

        // then
        assertThat(ExpectResult.getEmail()).isEqualTo(ActualResult.getEmail());
    }

    @Test
    public void 회원가입_실패_이메일중복() {
        // given
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .email("tkk@gamil.com")
                .password("PW")
                .name("syt")
                .phone("01011112222")
                .shoeSize(255)
                .build();

        // mocking
        given(memberRepository.existsByEmail(any())).willReturn(true);

        // when, then
        assertThrows(MemberDuplicationEmailException.class, () -> memberService.joinMember(memberJoinDto));
    }

    @Test
    public void 로그인_성공() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .build();

        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt@g.com")
                .password("PW")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(true);

        // when, then
        assertThatCode(() -> memberService.login(requestDto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("회원 이메일이 틀리면 MemberNotFoundException 예외가 발생한다.")
    public void 로그인_실패1() {
        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt@g.com")
                .password("PW")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.login(requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("비밀번호가 틀리면 LoginFailureException 예외가 발생한다.")
    public void 로그인_실패2() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .build();

        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt@g.com")
                .password("PW")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> memberService.login(requestDto))
                .isInstanceOf(LoginFailureException.class);
    }

}