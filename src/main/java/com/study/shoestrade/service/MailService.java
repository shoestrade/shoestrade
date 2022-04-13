package com.study.shoestrade.service;

import com.study.shoestrade.domain.mailAuth.MailAuth;
import com.study.shoestrade.exception.mailAuth.MailAuthNotEqualException;
import com.study.shoestrade.repository.MailAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailAuthRepository mailAuthRepository;

    public String sendMail(String email){
        String key = makeKey(email);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("ShoesTrade 회원가입 이메일 인증"); // 메일 제목
        message.setText("인증번호 : " + key);

        javaMailSender.send(message);

        return key;
    }

    public void saveKey(String email, String authKey){
        Optional<MailAuth> mailAuth = mailAuthRepository.findByEmail(email);

        if(mailAuth.isPresent()){
            mailAuth.get().changeAuthKey(authKey);
        }
        else{
            mailAuth = Optional.of(MailAuth.builder()
                    .email(email)
                    .authKey(authKey)
                    .build());
        }

        mailAuthRepository.save(mailAuth.get());
    }

    @Transactional(readOnly = true)
    public void checkKey(String email, String postKey){
        mailAuthRepository.findByEmailAndAuthKey(email, postKey)
                .orElseThrow(() -> new MailAuthNotEqualException("인증번호가 틀렸습니다."));
    }

    private String makeKey(String email){
        Random random = new Random();  // 난수 생성
        String key = "";  // 인증 번호

        for(int i = 0; i < 3; i++){
            int idx = random.nextInt(25) + 65;

            key += (char)idx;
        }

        int numIdx = random.nextInt(9999) + 1000;
        key += numIdx;
        return key;
    }
}
