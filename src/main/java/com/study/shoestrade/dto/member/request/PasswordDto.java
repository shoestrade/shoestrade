package com.study.shoestrade.dto.member.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PasswordDto {
    private String prePassword;
    private String newPassword;
}
