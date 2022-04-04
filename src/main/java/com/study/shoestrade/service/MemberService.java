package com.study.shoestrade.service;

import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.repository.MemberRepository;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.member.request.MemberJoinDto;
import com.study.shoestrade.exception.member.MemberDuplicationEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

}
