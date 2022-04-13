package com.study.shoestrade.domain.member;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "TOKEN_SEQ_GENERATOR", sequenceName = "TOKEN_SEQ")
@Builder
public class Token {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public void removeRefreshToken(){
        this.refreshToken = null;
    }
}
