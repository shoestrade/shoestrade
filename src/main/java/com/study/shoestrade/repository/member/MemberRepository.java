package com.study.shoestrade.repository.member;

import com.study.shoestrade.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Member> findByPhone(String phone);

    Optional<Member> findByEmailAndPhone(String email, String phone);

}
