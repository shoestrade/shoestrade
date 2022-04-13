package com.study.shoestrade.domain.mailAuth;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "MAIL_SEQ_GENERATOR", sequenceName = "MAIL_SEQ")
@AllArgsConstructor
@Builder
public class MailAuth {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MAIL_SEQ_GENERATOR")
    private Long id;

    private String email;
    private String authKey;

    public void changeAuthKey(String authKey){
        this.authKey = authKey;
    }
}
