package com.study.shoestrade.dto.member.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberFindResponseDto {
    private String email;
    private String password;
}
