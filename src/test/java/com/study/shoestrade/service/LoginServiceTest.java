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
import com.study.shoestrade.exception.member.WrongEmailException;
import com.study.shoestrade.exception.member.WrongPasswordException;
import com.study.shoestrade.exception.member.MemberDuplicationEmailException;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.token.InvalidRefreshTokenException;
import com.study.shoestrade.repository.MemberRepository;
import com.study.shoestrade.repository.TokenRepository;
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
        MemberDto ActualResult = loginService.joinMember(memberJoinDto);

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
        assertThrows(MemberDuplicationEmailException.class, () -> loginService.joinMember(memberJoinDto));
    }

    @Test
    @DisplayName("기존 토큰이 존재했으면 새로운 refreshToken을 생성한다.")
    public void 로그인_성공1() {
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
    @DisplayName("기존 토큰이 존재하지 않으면 token을 새로 생성한다.")
    public void 로그인_성공2() {
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
        given(tokenRepository.findByMember(1L)).willReturn(Optional.empty());
        given(jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole())).willReturn("accessToken");

        // when
        MemberLoginResponseDto responseDto = loginService.login(requestDto);

        // then
        assertThat(responseDto.getAccessToken()).isEqualTo("accessToken");
        assertThat(responseDto.getRefreshToken()).isEqualTo("newRefreshToken");
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
        assertThatThrownBy(() -> loginService.login(requestDto))
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
        assertThatThrownBy(() -> loginService.login(requestDto))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    public void 토큰_재발급_성공() {
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
    @DisplayName("accessToken의 email로 회원을 찾을 수 없으면 MemberNotFoundException 예외가 발생한다.")
    public void 토큰_재발급_실패2() {
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
    @DisplayName("회원의 토큰이 없으면 InvalidRefreshTokenException 예외가 발생한다.")
    public void 토큰_재발급_실패3() {
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
    @DisplayName("토큰의 refreshToken과 request의 refreshToken이 일치하지 않으면 InvalidRefreshTokenException 예외가 발생한다.")
    public void 토큰_재발급_실패4() {
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
    @DisplayName("member의 refreshToken이 일치하면 로그아웃이 성공한다.")
    public void 로그아웃_성공() {
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
    @DisplayName("member가 존재하지 않으면 MemberNotFoundException 예외가 발생한다.")
    public void 로그아웃_실패1() {
        // given, mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatCode(() -> loginService.logout("tt@g.com"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("token이 존재하지 않으면 InvalidRefreshTokenException 예외가 발생한다.")
    public void 로그아웃_실패2() {
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
        MemberFindResponseDto responseDto = loginService.findEmail(requestDto);

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
        assertThatThrownBy(() ->  loginService.findEmail(requestDto))
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
        MemberFindResponseDto responseDto = loginService.findPassword(requestDto);

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
        assertThatThrownBy(() -> loginService.findPassword(requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("회원이 존재하고 비밀번호가 일치하면 회원탈퇴가 성공한다.")
    public void 회원탈퇴_성공() {
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
    @DisplayName("입력한 email이 accessToken의 이메일과 일치하지 않으면 WrongEmailException 예외가 발생한다.")
    public void 회원탈퇴_실패1() {
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
    @DisplayName("이메일이 데이터베이스에 존재하지 않으면 MemberNotFoundException 예외가 발생한다.")
    public void 회원탈퇴_실패2() {
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
    @DisplayName("이메일이 일치하지만 입력한 비밀번호가 일치하지 않으면 WrongPasswordException 예외가 발생한다.")
    public void 회원탈퇴_실패3() {
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
    @DisplayName("member에 대한 token이 존재하지 않으면 InvalidRefreshTokenException 예외가 발생한다.")
    public void 회원탈퇴_실패4() {
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