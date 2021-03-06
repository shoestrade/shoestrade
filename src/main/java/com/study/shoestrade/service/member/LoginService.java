package com.study.shoestrade.service.member;

import com.study.shoestrade.common.config.jwt.JwtTokenProvider;
import com.study.shoestrade.domain.member.Role;
import com.study.shoestrade.domain.member.Token;
import com.study.shoestrade.dto.member.request.MemberFindRequestDto;
import com.study.shoestrade.dto.member.request.MemberLoginRequestDto;
import com.study.shoestrade.dto.member.request.TokenRequestDto;
import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.dto.member.response.MemberFindResponseDto;
import com.study.shoestrade.dto.member.response.MemberLoginResponseDto;
import com.study.shoestrade.exception.member.*;
import com.study.shoestrade.exception.token.InvalidRefreshTokenException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.member.request.MemberJoinDto;
import com.study.shoestrade.repository.member.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final TokenRepository tokenRepository;

    // ????????? ?????? ??????
    @Transactional(readOnly = true)
    public boolean checkEmailDuplication(String email) {
        return memberRepository.existsByEmail(email);
    }

    // ?????? ??????
    public MemberDto joinMember(MemberJoinDto memberJoinDto) {
        // ????????? ?????? ??????
        if (checkEmailDuplication(memberJoinDto.getEmail())) {
            throw new MemberDuplicationEmailException("?????? ??????????????? ????????? ?????????.");
        }

        Member member = memberRepository.save(
                memberJoinDto.toEntity(passwordEncoder.encode(memberJoinDto.getPassword())));
        return MemberDto.create(member);
    }

    // ?????????
    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new WrongPasswordException();
        }

        // ?????? ?????? ????????? ??????????????? ?????? ??????
        if(member.getBanReleaseTime() != null && member.getRole() == Role.BAN && LocalDateTime.now().isAfter(member.getBanReleaseTime())){
            log.info("?????? ????????? ???????????????.");
            member.changeRole(Role.ROLE_MEMBER);
        }

        // ?????? ?????? ??????
        if(member.getRole() == Role.BAN){
            throw new BanMemberException(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(member.getBanReleaseTime()));
        }

        String refreshToken = jwtTokenProvider.createRefreshToken();
        Optional<Token> optionalToken = tokenRepository.findByMember(member.getId());

        if (optionalToken.isPresent()) {
            Token token = optionalToken.get();
            token.updateRefreshToken(refreshToken);
        } else {
            Token token = Token.builder()
                    .refreshToken(refreshToken)
                    .member(member)
                    .build();

            tokenRepository.save(token);
        }

        return new MemberLoginResponseDto(
                jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole()),
                refreshToken);
    }

    // ?????? ?????????
    public MemberLoginResponseDto reIssue(TokenRequestDto requestDto) {
        Member member = findMemberByToken(requestDto);
        Token token = tokenRepository.findByMember(member.getId())
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!token.getRefreshToken().equals(requestDto.getRefreshToken())) {
            throw new InvalidRefreshTokenException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole());
        return new MemberLoginResponseDto(accessToken, requestDto.getRefreshToken());
    }

    private Member findMemberByToken(TokenRequestDto requestDto) {
        Authentication auth = jwtTokenProvider.getAuthentication(requestDto.getAccessToken());
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        return memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }

    /**
     * ??????????????? jwt accessToken ????????????.
     * @param email
     */
    // ????????????
    public void logout(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Token token = tokenRepository.findByMember(member.getId())
                .orElseThrow(InvalidRefreshTokenException::new);

        token.removeRefreshToken();
    }

    public String getLoginMemberEmail(ServletRequest request) {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        return jwtTokenProvider.getMemberEmail(token);
    }

    // ????????? ??????
    @Transactional(readOnly = true)
    public MemberFindResponseDto findEmail(MemberFindRequestDto requestDto) {
        Member findMember = memberRepository.findByPhone(requestDto.getPhone()).orElseThrow(MemberNotFoundException::new);

        String foundEmail = findMember.getEmail();
        String maskedEmail = emailMasking(foundEmail);

        return MemberFindResponseDto.builder()
                .email(maskedEmail)
                .build();
    }

    // ????????? ?????????
    private String emailMasking(String email) {
        String regex = "\\b(\\S+)+@(\\S+.\\S+)";

        Matcher matcher = Pattern.compile(regex).matcher(email);
        if (matcher.find()) {
            String target = matcher.group(1);
            int len = target.length();
            if (len > 2) {
                char[] c = new char[len - 2];
                Arrays.fill(c, '*');

                return email.replace(target, target.substring(0, 1) + String.valueOf(c) + target.substring(len - 1, len));
            }
        }

        return email;
    }

    // ???????????? ??????
    public MemberFindResponseDto findPassword(MemberFindRequestDto requestDto) {
        Member findMember = memberRepository.findByEmailAndPhone(requestDto.getEmail(), requestDto.getPhone())
                .orElseThrow(MemberNotFoundException::new);

        String randomPassword = getRandomPassword();
        findMember.changePassword(passwordEncoder.encode(randomPassword));

        javaMailSender.send(setRandomPasswordMail(requestDto.getEmail(), randomPassword));

        return MemberFindResponseDto.builder()
                .email(requestDto.getEmail())
                .password(randomPassword)
                .build();
    }

    // ?????? ???????????? ??????
    private String getRandomPassword() {
        char[] ch = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '@'};

        StringBuilder sb = new StringBuilder();

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (ch.length * Math.random());
            sb.append(ch[idx]);
        }
        return sb.toString();
    }

    // ?????? ???????????? ?????? ??????
    private SimpleMailMessage setRandomPasswordMail(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("ShoesTrade ?????? ????????????");
        message.setText("?????? ???????????? : " + password);

        return message;
    }


    /**
     * ????????? jwt accessToken ?????????
     * ?????? ??????
     * ???????????? ????????? ????????? ?????? ?????? ?????????
     * ???????????? ????????? ?????? ??????
     */
    // ?????? ??????
    public void deleteMember(String email, MemberLoginRequestDto requestDto) {
        if (!email.equals(requestDto.getEmail())){
            throw new WrongEmailException();
        }

        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(requestDto.getPassword(), findMember.getPassword())) {
            throw new WrongPasswordException();
        }

        Token token = tokenRepository.findByMember(findMember.getId())
                .orElseThrow(InvalidRefreshTokenException::new);

        tokenRepository.delete(token);
        memberRepository.delete(findMember);
    }

}
