package com.study.shoestrade.dto.member.response;

import com.study.shoestrade.domain.member.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginResponseDto {

    private Long id;
    private String email;
    private Role role;
}
