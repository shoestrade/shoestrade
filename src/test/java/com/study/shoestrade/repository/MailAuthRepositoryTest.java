package com.study.shoestrade.repository;

import com.study.shoestrade.domain.mailAuth.MailAuth;
import com.study.shoestrade.repository.member.MailAuthRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MailAuthRepositoryTest {

    @Autowired
    MailAuthRepository mailAuthRepository;

    @Test
    public void 이메일_검색() {
        // given
        MailAuth mailAuth = MailAuth.builder()
                .email("tkk@gamil.com")
                .authKey("1234")
                .build();

        mailAuthRepository.save(mailAuth);

        // when
        MailAuth findByEmailSuccess = mailAuthRepository.findByEmail("tkk@gamil.com").orElse(null);
        MailAuth findByEmailFail = mailAuthRepository.findByEmail("as").orElse(null);

        // then
        assertThat(findByEmailSuccess).isEqualTo(mailAuth);
        assertThat(findByEmailFail).isNull();
    }

    @Test
    public void 이메일_키_검색() {
        // given
        MailAuth mailAuth = MailAuth.builder()
                .email("tkk@gamil.com")
                .authKey("1234")
                .build();

        mailAuthRepository.save(mailAuth);

        // when
        MailAuth success = mailAuthRepository.findByEmailAndAuthKey("tkk@gamil.com", "1234").orElse(null);
        MailAuth fail1 = mailAuthRepository.findByEmailAndAuthKey("tkk@gamil.com", "1111").orElse(null);
        MailAuth fail2 = mailAuthRepository.findByEmailAndAuthKey("tkk@.com", "1234").orElse(null);
        MailAuth fail3 = mailAuthRepository.findByEmailAndAuthKey("tkk@.com", "1133").orElse(null);

        // then
        assertThat(success).isEqualTo(mailAuth);
        assertThat(fail1).isNull();
        assertThat(fail2).isNull();
        assertThat(fail3).isNull();
    }
}