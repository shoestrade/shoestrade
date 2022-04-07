package com.study.shoestrade.service;

import com.study.shoestrade.dto.member.request.MemberLoginRequestDto;
import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.dto.member.response.MemberLoginResponseDto;
import com.study.shoestrade.exception.member.LoginFailureException;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.repository.MemberRepository;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.member.request.MemberJoinDto;
import com.study.shoestrade.exception.member.MemberDuplicationEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;

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

   // 로그인
   @Transactional(readOnly = true)
   public MemberLoginResponseDto login(MemberLoginRequestDto requestDto){
       Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(MemberNotFoundException::new);

       if(!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())){
           throw new LoginFailureException();
       }

       session.setAttribute("MEMBER_EMAIL", member.getEmail());
       session.setAttribute("MEMBER_ROLE", member.getRole());

       return new MemberLoginResponseDto(member.getId(), member.getEmail(), member.getRole());
   }

    // 로그아웃
    public void logout(){
       session.removeAttribute("MEMBER_EMAIL");
    }

}
