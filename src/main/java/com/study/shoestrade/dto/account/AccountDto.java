package com.study.shoestrade.dto.account;

import com.study.shoestrade.domain.member.Account;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ApiModel
public class AccountDto {
    @ApiModelProperty(example = "농협", value = "은행명")
    private String bankName;

    @ApiModelProperty(example = "00000000000000", value = "계좌 번호 (-는 빼고 작성)")
    private String accountNumber;

    @ApiModelProperty(example = "서영탁", value = "예금주")
    private String accountHolder;

    public Account toEntity() {
        return Account.builder()
                .bankName(bankName)
                .accountNumber(accountNumber)
                .accountHolder(accountHolder)
                .build();
    }

    public static AccountDto create(Account account){
        return AccountDto.builder()
                .accountHolder(account.getAccountHolder())
                .accountNumber(account.getAccountNumber())
                .bankName(account.getBankName())
                .build();
    }
}
