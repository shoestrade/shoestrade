package com.study.shoestrade.common.config.security.member;

import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // username이 DB에 있는지 확인
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername={}", username);

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        return MemberDetails.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .build();
    }
}
