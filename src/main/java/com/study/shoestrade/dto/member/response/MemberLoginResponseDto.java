package com.study.shoestrade.dto.member.response;

import com.study.shoestrade.domain.member.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDto {

    private Long id;
    private String email;
    private Role role;
}
