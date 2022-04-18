package com.study.shoestrade.dto.admin.response;

import com.study.shoestrade.domain.member.Grade;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.member.Role;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PageMemberDto {
    private Long id;
    private String email;
    private String name;
    private Grade grade;
    private int point;
    private Role role;

    public static PageMemberDto create(Member member){
        return PageMemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .grade(member.getGrade())
                .point(member.getPoint())
                .role(member.getRole())
                .build();
    }
}
