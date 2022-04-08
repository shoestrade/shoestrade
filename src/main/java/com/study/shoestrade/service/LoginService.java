package com.study.shoestrade.service;

import com.study.shoestrade.dto.member.request.MemberFindRequestDto;
import com.study.shoestrade.dto.member.request.MemberLoginRequestDto;
import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.dto.member.response.MemberFindResponseDto;
import com.study.shoestrade.dto.member.response.MemberLoginResponseDto;
import com.study.shoestrade.exception.member.WrongPasswordException;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.repository.MemberRepository;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.member.request.MemberJoinDto;
import com.study.shoestrade.exception.member.MemberDuplicationEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;
    private final JavaMailSender javaMailSender;

    // 이메일 중복 체크
   @Transactional(readOnly = true)
    public boolean checkEmailDuplication(String email){
       log.info("이메일 중복 체크");
       return memberRepository.existsByEmail(email);
   }

   public MemberDto joinMember(MemberJoinDto memberJoinDto){
       log.info("회원가입");

       // 이메일 중복 체크
       if(checkEmailDuplication(memberJoinDto.getEmail())){
            throw new MemberDuplicationEmailException("이미 회원가입된 이메일 입니다.");
       }

       Member member = memberRepository.save(
               memberJoinDto.toEntity(passwordEncoder.encode(memberJoinDto.getPassword())));
       return MemberDto.create(member);
   }

    /**
     * 추가 필요
     * 벤 당했는 지 검사 필요
     */
   // 로그인
   @Transactional(readOnly = true)
   public MemberLoginResponseDto login(MemberLoginRequestDto requestDto){
       log.info("Login -> login 실행");
       Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(MemberNotFoundException::new);

       if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
           throw new WrongPasswordException();
       }

       session.setAttribute("MEMBER_EMAIL", member.getEmail());
       session.setAttribute("MEMBER_ROLE", member.getRole());

       return new MemberLoginResponseDto(member.getId(), member.getEmail(), member.getRole());
   }

    // 로그아웃
    public void logout(){
       session.removeAttribute("MEMBER_EMAIL");
    }

    public String getLoginMember(){
       return (String) session.getAttribute("MEMBER_EMAIL");
    }

    // 이메일 찾기
    @Transactional(readOnly = true)
    public MemberFindResponseDto findEmail(MemberFindRequestDto requestDto){
       log.info("LoginService -> findEmail 실행");

       Member findMember = memberRepository.findByPhone(requestDto.getPhone()).orElseThrow(MemberNotFoundException::new);

       String foundEmail = findMember.getEmail();
       String maskedEmail = emailMasking(foundEmail);

       return MemberFindResponseDto.builder()
               .email(maskedEmail)
               .build();
    }

    // 이메일 마스킹
    private String emailMasking(String email){
       String regex = "\\b(\\S+)+@(\\S+.\\S+)";

        Matcher matcher = Pattern.compile(regex).matcher(email);
        if(matcher.find()){
            String target = matcher.group(1);
            int len = target.length();
            if(len > 2){
                char[] c = new char[len - 2];
                Arrays.fill(c, '*');

                return email.replace(target, target.substring(0, 1) + String.valueOf(c) + target.substring(len-1,len));
            }
        }

        return email;
    }

    // 비밀번호 찾기
    public MemberFindResponseDto findPassword(MemberFindRequestDto requestDto){
       log.info("LoginService -> findPassword 실행");

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

    // 랜덤 비밀번호 생성
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

    // 임시 비밀번호 전송 메일
    private SimpleMailMessage setRandomPasswordMail(String email, String password){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("ShoesTrade 임시 비밀번호");
        message.setText("임시 비밀번호 : " + password);

        return message;
    }

    /**
     * 추가 필요
     * 진행중인 거래가 있으면 회원 탈퇴 불가능
     * 포인트가 있으면 확인 필요
     */
    // 회원 탈퇴
    public MemberLoginResponseDto deleteMember(MemberLoginRequestDto requestDto){
       log.info("LoginService -> deleteMember 실행");

        Member findMember = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if(!passwordEncoder.matches(requestDto.getPassword(), findMember.getPassword())){
            throw new WrongPasswordException();
        }

       memberRepository.deleteByEmail(findMember.getEmail());
       return MemberLoginResponseDto.builder()
               .email(requestDto.getEmail())
               .build();
    }
}
