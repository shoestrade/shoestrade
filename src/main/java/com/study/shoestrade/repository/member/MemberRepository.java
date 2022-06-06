package com.study.shoestrade.repository.member;

import com.study.shoestrade.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Member> findByPhone(String phone);

    Optional<Member> findByEmailAndPhone(String email, String phone);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.warningCount = m.warningCount + :count where m.id = :memberId")
    void updateMemberWaringCount(@Param("count") int count, @Param("memberId") Long memberId);

    @Query("select m from Member m where m.id in :list")
    List<Member> findWarnedMembers(@Param("list") List<Long> list);
}
