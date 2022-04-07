package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.member.request.MemberFindRequestDto;
import com.study.shoestrade.dto.member.request.MemberJoinDto;
import com.study.shoestrade.dto.member.request.MemberLoginRequestDto;
import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.dto.member.response.MemberFindResponseDto;
import com.study.shoestrade.exception.member.LoginFailureException;
import com.study.shoestrade.exception.member.MemberDuplicationEmailException;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
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
    @Mock
    JavaMailSender javaMailSender;

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

    @Test
    public void 이메일_찾기_성공() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt12@gmail.com")
                .phone("01011111111")
                .password("PW")
                .build();

        MemberFindRequestDto requestDto = MemberFindRequestDto.builder()
                .phone("01011111111")
                .build();

        // mocking
        given(memberRepository.findByPhone(any())).willReturn(Optional.of(member));

        // when
        MemberFindResponseDto responseDto = memberService.findEmail(requestDto);

        // then
        assertThat(responseDto.getEmail()).isEqualTo("t**2@gmail.com");
        assertThat(responseDto.getPassword()).isNull();
    }

    @Test
    @DisplayName("휴대폰 번호를 잘 못입력하여 member가 없으면 MemberNotFoundException 예외가 발생한다.")
    public void 이메일_찾기_실패() {
        // given
        MemberFindRequestDto requestDto = MemberFindRequestDto.builder()
                .phone("01011111111")
                .build();

        // mocking
        given(memberRepository.findByPhone(any())).willReturn(Optional.empty());

        //  when, then
        assertThatThrownBy(() ->  memberService.findEmail(requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("비밀번호 찾기를 시도하면 기존의 비밀번호가 랜덤값으로 변경된다.")
    public void 비밀번호_찾기_성공() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt12@gmail.com")
                .phone("01011111111")
                .password("PW")
                .build();

        MemberFindRequestDto requestDto = MemberFindRequestDto.builder()
                .email("tt12@gmail.com")
                .phone("01011111111")
                .build();

        // mocking
        given(memberRepository.findByEmailAndPhone(any(), any())).willReturn(Optional.of(member));

        // when
        MemberFindResponseDto responseDto = memberService.findPassword(requestDto);

        // then
        assertThat(responseDto.getPassword()).isNotEqualTo(member.getPassword());
    }

    @Test
    @DisplayName("이메일 또는 휴대폰 번호가 틀리면 MemberNotFoundException 예외가 발생한다.")
    public void 비밀번호_찾기_실패() {
        // given
        MemberFindRequestDto requestDto = MemberFindRequestDto.builder()
                .email("tt12@gmail.com")
                .phone("01011111111")
                .build();

        // mocking
        given(memberRepository.findByEmailAndPhone(any(), any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.findPassword(requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

}