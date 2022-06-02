package com.study.shoestrade.service.member;

import com.study.shoestrade.domain.mailAuth.MailAuth;
import com.study.shoestrade.exception.mailAuth.MailAuthNotEqualException;
import com.study.shoestrade.exception.member.MemberDuplicationEmailException;
import com.study.shoestrade.repository.member.MailAuthRepository;
import com.study.shoestrade.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailAuthRepository mailAuthRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public boolean checkEmailDuplication(String email) {
        return memberRepository.existsByEmail(email);
    }

    public String sendAuthMail(String email){
        if (checkEmailDuplication(email)) {
            throw new MemberDuplicationEmailException("이미 회원가입된 이메일 입니다.");
        }

        String key = makeKey();

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

    // 청구 메일 전송
    public void sendClaimMail(String email, LocalDateTime deadline){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("구매 입찰 상품 체결"); // 메일 제목
        message.setText("구매 입찰하신 상품이 체결되었습니다.");
        message.setText(deadline.format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")) + "까지 청구하셔야합니다.");

        javaMailSender.send(message);
    }

    private String makeKey(){
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
