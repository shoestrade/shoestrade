package com.study.shoestrade.repository;

import com.study.shoestrade.domain.mailAuth.MailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailAuthRepository extends JpaRepository<MailAuth, Long> {
    Optional<MailAuth> findByEmail(String email);
    Optional<MailAuth> findByEmailAndAuthKey(String email, String key);
}
