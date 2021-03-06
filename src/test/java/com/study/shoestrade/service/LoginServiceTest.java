package com.study.shoestrade.service;

import com.study.shoestrade.common.config.jwt.JwtTokenProvider;
import com.study.shoestrade.common.config.security.member.MemberDetails;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.member.Role;
import com.study.shoestrade.domain.member.Token;
import com.study.shoestrade.dto.member.request.MemberFindRequestDto;
import com.study.shoestrade.dto.member.request.MemberJoinDto;
import com.study.shoestrade.dto.member.request.MemberLoginRequestDto;
import com.study.shoestrade.dto.member.request.TokenRequestDto;
import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.dto.member.response.MemberFindResponseDto;
import com.study.shoestrade.dto.member.response.MemberLoginResponseDto;
import com.study.shoestrade.exception.member.*;
import com.study.shoestrade.exception.token.InvalidRefreshTokenException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.member.TokenRepository;
import com.study.shoestrade.service.member.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    LoginService loginService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JavaMailSender javaMailSender;
    @Mock
    JwtTokenProvider jwtTokenProvider;
    @Mock
    TokenRepository tokenRepository;

    @Test
    public void ????????????_??????() {
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
        MemberDto ActualResult = loginService.joinMember(memberJoinDto);

        // then
        assertThat(ExpectResult.getEmail()).isEqualTo(ActualResult.getEmail());
    }

    @Test
    public void ????????????_??????_???????????????() {
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
        assertThrows(MemberDuplicationEmailException.class, () -> loginService.joinMember(memberJoinDto));
    }

    @Test
    @DisplayName("?????? ????????? ??????????????? ????????? refreshToken??? ????????????.")
    public void ?????????_??????1() {
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

        Token token = Token.builder()
                .id(1L)
                .refreshToken("refreshToken")
                .member(member)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        given(jwtTokenProvider.createRefreshToken()).willReturn("newRefreshToken");
        given(tokenRepository.findByMember(1L)).willReturn(Optional.of(token));
        given(jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole())).willReturn("accessToken");

        // when
        MemberLoginResponseDto responseDto = loginService.login(requestDto);

        // then
        assertThat(responseDto.getAccessToken()).isEqualTo("accessToken");
        assertThat(responseDto.getRefreshToken()).isEqualTo("newRefreshToken");
    }

    @Test
    @DisplayName("?????? ????????? ???????????? ????????? token??? ?????? ????????????.")
    public void ?????????_??????2() {
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
        given(jwtTokenProvider.createRefreshToken()).willReturn("newRefreshToken");
        given(tokenRepository.findByMember(1L)).willReturn(Optional.empty());
        given(jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole())).willReturn("accessToken");

        // when
        MemberLoginResponseDto responseDto = loginService.login(requestDto);

        // then
        assertThat(responseDto.getAccessToken()).isEqualTo("accessToken");
        assertThat(responseDto.getRefreshToken()).isEqualTo("newRefreshToken");
    }

    @Test
    @DisplayName("?????? ????????? ?????? ????????? ????????? ??? ????????? role??? ???????????? ???????????? ????????????.")
    public void ?????????_??????3() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .role(Role.BAN)
                .banReleaseTime(LocalDateTime.of(2022, 2, 2, 2, 2, 2))
                .build();

        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt@g.com")
                .password("PW")
                .build();

        Token token = Token.builder()
                .id(1L)
                .refreshToken("refreshToken")
                .member(member)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        given(jwtTokenProvider.createRefreshToken()).willReturn("newRefreshToken");
        given(tokenRepository.findByMember(1L)).willReturn(Optional.of(token));
        given(jwtTokenProvider.createAccessToken(any(), any())).willReturn("accessToken");

        // when
        MemberLoginResponseDto responseDto = loginService.login(requestDto);

        // then dh
        assertThat(responseDto.getAccessToken()).isEqualTo("accessToken");
        assertThat(responseDto.getRefreshToken()).isEqualTo("newRefreshToken");
        assertThat(member.getRole()).isEqualTo(Role.ROLE_MEMBER);
    }

    @Test
    @DisplayName("?????? ???????????? ????????? MemberNotFoundException ????????? ????????????.")
    public void ?????????_??????1() {
        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt@g.com")
                .password("PW")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> loginService.login(requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("??????????????? ????????? LoginFailureException ????????? ????????????.")
    public void ?????????_??????2() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .build();

        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt@g.com")
                .password("wrong PW")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> loginService.login(requestDto))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    @DisplayName("????????? ???????????? ????????? BanMemberException ????????? ????????????.")
    public void ?????????_??????3() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .role(Role.BAN)
                .banReleaseTime(LocalDateTime.of(2222, 2, 2, 2, 2, 2))
                .build();

        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt@g.com")
                .password("PW")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(true);

        // when, then
        assertThatCode(() -> loginService.login(requestDto))
                .isInstanceOf(BanMemberException.class);
    }

    @Test
    public void ??????_?????????_??????() {
        // given
        TokenRequestDto requestDto = TokenRequestDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        MemberDetails memberDetails = MemberDetails.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .role(Role.ROLE_MEMBER)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(memberDetails,"", memberDetails.getAuthorities());

        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .build();

        Token token = Token.builder()
                .id(1L)
                .refreshToken("refreshToken")
                .member(member)
                .build();

        // mocking
        given(jwtTokenProvider.getAuthentication(any())).willReturn(auth);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(tokenRepository.findByMember(any())).willReturn(Optional.of(token));
        given(jwtTokenProvider.createAccessToken(any(), any())).willReturn("newAccessToken");

        // when
        MemberLoginResponseDto responseDto = loginService.reIssue(requestDto);

        // then
        assertThat(responseDto.getAccessToken()).isEqualTo("newAccessToken");
        assertThat(responseDto.getRefreshToken()).isEqualTo("refreshToken");
    }

    @Test
    @DisplayName("accessToken??? email??? ????????? ?????? ??? ????????? MemberNotFoundException ????????? ????????????.")
    public void ??????_?????????_??????2() {
        // given
        TokenRequestDto requestDto = TokenRequestDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        MemberDetails memberDetails = MemberDetails.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .role(Role.ROLE_MEMBER)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(memberDetails,"", memberDetails.getAuthorities());

        // mocking
        given(jwtTokenProvider.getAuthentication(any())).willReturn(auth);
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatCode(() -> loginService.reIssue(requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("????????? ????????? ????????? InvalidRefreshTokenException ????????? ????????????.")
    public void ??????_?????????_??????3() {
        // given
        TokenRequestDto requestDto = TokenRequestDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        MemberDetails memberDetails = MemberDetails.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .role(Role.ROLE_MEMBER)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(memberDetails,"", memberDetails.getAuthorities());

        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .build();

        // mocking
        given(jwtTokenProvider.getAuthentication(any())).willReturn(auth);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(tokenRepository.findByMember(any())).willReturn(Optional.empty());

        // when, then
        assertThatCode(() -> loginService.reIssue(requestDto))
                .isInstanceOf(InvalidRefreshTokenException.class);
    }

    @Test
    @DisplayName("????????? refreshToken??? request??? refreshToken??? ???????????? ????????? InvalidRefreshTokenException ????????? ????????????.")
    public void ??????_?????????_??????4() {
        // given
        TokenRequestDto requestDto = TokenRequestDto.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        MemberDetails memberDetails = MemberDetails.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .role(Role.ROLE_MEMBER)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(memberDetails,"", memberDetails.getAuthorities());

        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .build();

        Token token = Token.builder()
                .id(1L)
                .refreshToken("DifferentRefreshToken")
                .member(member)
                .build();

        // mocking
        given(jwtTokenProvider.getAuthentication(any())).willReturn(auth);
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(tokenRepository.findByMember(any())).willReturn(Optional.of(token));

        // when, then
        assertThatCode(() -> loginService.reIssue(requestDto))
                .isInstanceOf(InvalidRefreshTokenException.class);
    }

    @Test
    @DisplayName("member??? refreshToken??? ???????????? ??????????????? ????????????.")
    public void ????????????_??????() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .build();

        Token token = Token.builder()
                .id(1L)
                .refreshToken("refreshToken")
                .member(member)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(tokenRepository.findByMember(member.getId())).willReturn(Optional.of(token));

        // when
        loginService.logout("tt@g.com");

        // then
        assertThat(token.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("member??? ???????????? ????????? MemberNotFoundException ????????? ????????????.")
    public void ????????????_??????1() {
        // given, mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatCode(() -> loginService.logout("tt@g.com"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("token??? ???????????? ????????? InvalidRefreshTokenException ????????? ????????????.")
    public void ????????????_??????2() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(tokenRepository.findByMember(member.getId())).willReturn(Optional.empty());

        // when, then
        assertThatCode(() -> loginService.logout("tt@g.com"))
                .isInstanceOf(InvalidRefreshTokenException.class);
    }

    @Test
    public void ?????????_??????_??????() {
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
        MemberFindResponseDto responseDto = loginService.findEmail(requestDto);

        // then
        assertThat(responseDto.getEmail()).isEqualTo("t**2@gmail.com");
        assertThat(responseDto.getPassword()).isNull();
    }

    @Test
    @DisplayName("????????? ????????? ??? ??????????????? member??? ????????? MemberNotFoundException ????????? ????????????.")
    public void ?????????_??????_??????() {
        // given
        MemberFindRequestDto requestDto = MemberFindRequestDto.builder()
                .phone("01011111111")
                .build();

        // mocking
        given(memberRepository.findByPhone(any())).willReturn(Optional.empty());

        //  when, then
        assertThatThrownBy(() ->  loginService.findEmail(requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("???????????? ????????? ???????????? ????????? ??????????????? ??????????????? ????????????.")
    public void ????????????_??????_??????() {
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
        MemberFindResponseDto responseDto = loginService.findPassword(requestDto);

        // then
        assertThat(responseDto.getPassword()).isNotEqualTo(member.getPassword());
    }

    @Test
    @DisplayName("????????? ?????? ????????? ????????? ????????? MemberNotFoundException ????????? ????????????.")
    public void ????????????_??????_??????() {
        // given
        MemberFindRequestDto requestDto = MemberFindRequestDto.builder()
                .email("tt12@gmail.com")
                .phone("01011111111")
                .build();

        // mocking
        given(memberRepository.findByEmailAndPhone(any(), any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> loginService.findPassword(requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("????????? ???????????? ??????????????? ???????????? ??????????????? ????????????.")
    public void ????????????_??????() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt12@gmail.com")
                .phone("01011111111")
                .password("PW")
                .build();

        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt12@gmail.com")
                .password("PW")
                .build();

        Token token = Token.builder()
                .id(1L)
                .refreshToken("refreshToken")
                .member(member)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        given(tokenRepository.findByMember(any())).willReturn(Optional.of(token));

        // when, then
        assertThatCode(() -> loginService.deleteMember("tt12@gmail.com", requestDto))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("????????? email??? accessToken??? ???????????? ???????????? ????????? WrongEmailException ????????? ????????????.")
    public void ????????????_??????1() {
        // given
        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt12@gmail.com")
                .password("PW")
                .build();

        // when, then
        assertThatCode(() -> loginService.deleteMember("wrongEmail", requestDto))
                .isInstanceOf(WrongEmailException.class);
    }

    @Test
    @DisplayName("???????????? ????????????????????? ???????????? ????????? MemberNotFoundException ????????? ????????????.")
    public void ????????????_??????2() {
        // given
        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt12@gmail.com")
                .password("PW")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> loginService.deleteMember("tt12@gmail.com", requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("???????????? ??????????????? ????????? ??????????????? ???????????? ????????? WrongPasswordException ????????? ????????????.")
    public void ????????????_??????3() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt12@gmail.com")
                .phone("01011111111")
                .password("PW")
                .build();

        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt12@gmail.com")
                .password("WrongPassword")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> loginService.deleteMember("tt12@gmail.com", requestDto))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    @DisplayName("member??? ?????? token??? ???????????? ????????? InvalidRefreshTokenException ????????? ????????????.")
    public void ????????????_??????4() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt12@gmail.com")
                .phone("01011111111")
                .password("PW")
                .build();

        MemberLoginRequestDto requestDto = MemberLoginRequestDto.builder()
                .email("tt12@gmail.com")
                .password("WrongPassword")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        given(tokenRepository.findByMember(any())).willReturn(Optional.empty());

        // when, then
        assertThatCode(() -> loginService.deleteMember("tt12@gmail.com", requestDto))
                .isInstanceOf(InvalidRefreshTokenException.class);
    }

}