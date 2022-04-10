package com.study.shoestrade.dto.member.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberFindRequestDto {
    private String email;
    private String phone;
}
