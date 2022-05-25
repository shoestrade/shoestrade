package com.study.shoestrade.dto.member.request;


import com.study.shoestrade.domain.member.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberJoinDto {
    @ApiModelProperty(example = "cjswltjr159@naver.com",value = "email")
    private String email;

    @ApiModelProperty(example = "123123a", value = "비밀번호")
    private String password;

    @ApiModelProperty(example = "천지석",value = "이름")
    private String name;

    @ApiModelProperty(example = "010-1234-1234",value = "전화번호")
    private String phone;

    @ApiModelProperty(example = "255",value = "회원의 발 사이즈")
    private int shoeSize;

    public Member toEntity(String password) {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .point(0)
                .grade(Grade.BRONZE)
                .shoeSize(shoeSize)
                .role(Role.ROLE_MEMBER)
                .warningCount(0L)
                .banCount(0L)
                .build();
    }
}
