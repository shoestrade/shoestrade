package com.study.shoestrade.dto.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginRequestDto {
    @ApiModelProperty(example = "cjswltjr159@naver.com",value = "이메일")
    private String email;

    @ApiModelProperty(example = "12341234a",value = "비밀번호")
    private String password;
}
