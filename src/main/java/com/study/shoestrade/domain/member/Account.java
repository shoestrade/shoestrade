package com.study.shoestrade.domain.member;

import com.study.shoestrade.dto.account.AccountDto;
import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Account {

    @NotNull
    private String bankName; // 은행이름

    @NotNull
    private String accountNumber; // 계좌번호

    @NotNull
    private String accountHolder; // 예금주
}
