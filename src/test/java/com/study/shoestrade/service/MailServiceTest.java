package com.study.shoestrade.service;

import com.study.shoestrade.domain.mailAuth.MailAuth;
import com.study.shoestrade.exception.mailAuth.MailAuthNotEqualException;
import com.study.shoestrade.repository.member.MailAuthRepository;
import com.study.shoestrade.service.member.MailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @InjectMocks
    MailService mailService;

    @Mock
    MailAuthRepository mailAuthRepository;
    @Mock
    JavaMailSender javaMailSender;  // 이건 테스트때 필요없을꺼 같은데?

    private MailAuth mailAuth;

    @BeforeEach
    public void init(){
        mailService = new MailService(javaMailSender, mailAuthRepository);

        mailAuth = MailAuth.builder()
                .email("tkk@gamil.com")
                .authKey("1234")
                .build();
    }
    
    @Test
    public void 인증번호_검증_성공() {
        // given

        // mocking
        given(mailAuthRepository.findByEmailAndAuthKey(any(), any())).willReturn(Optional.ofNullable(mailAuth));

        // when, then
        assertThatCode(() -> mailService.checkKey(mailAuth.getEmail(), mailAuth.getAuthKey()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("인증번호가 틀리면 MailAuthNotEqualException 예외가 발생한다.")
    public void 인증번호_검증_실패() {
        // given

        // mocking
        given(mailAuthRepository.findByEmailAndAuthKey(any(), any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> mailService.checkKey(mailAuth.getEmail(), mailAuth.getAuthKey()))
                .isInstanceOf(MailAuthNotEqualException.class);
    }

}