package com.study.shoestrade.repository.member;

import com.study.shoestrade.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Member> findMembers(String email, Pageable pageable);
}
