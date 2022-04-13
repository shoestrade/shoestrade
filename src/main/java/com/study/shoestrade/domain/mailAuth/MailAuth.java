package com.study.shoestrade.domain.mailAuth;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MailAuth {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String authKey;

    public void changeAuthKey(String authKey){
        this.authKey = authKey;
    }
}
