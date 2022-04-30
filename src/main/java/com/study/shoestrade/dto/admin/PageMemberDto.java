package com.study.shoestrade.dto.admin;

import com.study.shoestrade.domain.member.Grade;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.member.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PageMemberDto {
    @ApiModelProperty(example = "1", value = "회원의 id")
    private Long id;

    @ApiModelProperty(example = "cjswltjr159@naver.com",value = "이메일")
    private String email;

    @ApiModelProperty(example = "서영탁", value = "이름")
    private String name;

    @ApiModelProperty(example = "BRONZE",value = "회원등급")
    private Grade grade;

    @ApiModelProperty(example = "1000",value = "포인트")
    private int point;

    @ApiModelProperty(example = "ROLE_MEMBER", value = "회원 권한 구분")
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
