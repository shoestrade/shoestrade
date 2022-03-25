package com.study.shoestrade.domain.member;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Account {

    @NotNull
    private String bankName; // 은행이름

    @NotNull
    private Long accountNumber; // 계좌번호

    @NotNull
    private String accountHolder; // 예금주
}
