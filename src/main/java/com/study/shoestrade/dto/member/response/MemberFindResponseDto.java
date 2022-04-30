package com.study.shoestrade.dto.member.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberFindResponseDto {
    @ApiModelProperty(example = "cjswltjr159@naver.com", value = "email")
    private String email;

    @ApiModelProperty(example = "12314a", value = "비밀번호")
    private String password;
}
