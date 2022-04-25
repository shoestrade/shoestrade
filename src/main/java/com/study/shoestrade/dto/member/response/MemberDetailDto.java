package com.study.shoestrade.dto.member.response;

import com.study.shoestrade.domain.member.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberDetailDto {

    private String email;
    private String name;
    private String phone;
    private int shoeSize;
    private Grade grade;
    private int point;
    private Account account;
    private Role role;
    private LocalDateTime banReleaseTime;

    public static MemberDetailDto create(Member member){
        return MemberDetailDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .shoeSize(member.getShoeSize())
                .grade(member.getGrade())
                .point(member.getPoint())
                .account(member.getAccount())
                .role(member.getRole())
                .banReleaseTime(member.getBanReleaseTime())
                .build();
    }
}
