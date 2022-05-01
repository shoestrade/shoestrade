package com.study.shoestrade.dto.member.response;

import com.study.shoestrade.domain.member.*;
import com.study.shoestrade.dto.account.AccountDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberDetailDto {

    @ApiModelProperty(example = "cjswltjr159@naver.com", value = "이메일")
    private String email;

    @ApiModelProperty(example = "서영탁", value = "이름")
    private String name;

    @ApiModelProperty(example = "010-1234-1234", value = "휴대폰 번호")
    private String phone;

    @ApiModelProperty(example = "255", value = "신발 사이즈")
    private int shoeSize;

    @ApiModelProperty(example = "BRONZE", value = "회원 등급")
    private Grade grade;

    @ApiModelProperty(example = "1000", value = "포인트")
    private int point;

    private AccountDto account;

    @ApiModelProperty(example = "ROLE_MEMBER", value = "회원 권한")
    private Role role;

    @ApiModelProperty(example = "2022-04-30 12:12:12", value = "정지 해제 날짜")
    private LocalDateTime banReleaseTime;

    public static MemberDetailDto create(Member member){
        return MemberDetailDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .shoeSize(member.getShoeSize())
                .grade(member.getGrade())
                .point(member.getPoint())
                .account(AccountDto.create(member.getAccount()))
                .role(member.getRole())
                .banReleaseTime(member.getBanReleaseTime())
                .build();
    }
}
