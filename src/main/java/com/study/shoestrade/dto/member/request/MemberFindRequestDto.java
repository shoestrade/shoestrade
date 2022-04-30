package com.study.shoestrade.dto.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberFindRequestDto {
    @ApiModelProperty(example = "cjswltjr159@naver.com", value = "email (비밀번호 찾기 시에만 key 사용)")
    private String email;

    @ApiModelProperty(example = "010-1234-1234", value = "휴대폰 번호")
    private String phone;
}
