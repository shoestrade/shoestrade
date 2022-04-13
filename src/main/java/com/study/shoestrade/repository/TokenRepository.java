package com.study.shoestrade.repository;

import com.study.shoestrade.domain.member.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t from Token t join fetch t.member m where m.id = :memberId")
    Optional<Token> findByMember(@Param("memberId") Long memberId);
}
