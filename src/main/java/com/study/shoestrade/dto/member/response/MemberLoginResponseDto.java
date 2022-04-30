package com.study.shoestrade.dto.member.response;

import com.study.shoestrade.domain.member.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLoginResponseDto {

    @ApiModelProperty(value = "로그인시 발급된 accessToken")
    private String accessToken;

    @ApiModelProperty(value = "로그인시 발급된 refreshToken")
    private String refreshToken;
}
