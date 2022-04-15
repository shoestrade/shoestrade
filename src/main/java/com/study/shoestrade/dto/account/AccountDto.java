package com.study.shoestrade.dto.account;

import com.study.shoestrade.domain.member.Account;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AccountDto {

    private String bankName;
    private String accountNumber;
    private String accountHolder;

    public Account toEntity(){
        return Account.builder()
                .bankName(bankName)
                .accountNumber(accountNumber)
                .accountHolder(accountHolder)
                .build();
    }
}
