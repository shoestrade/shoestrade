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
public class TokenRequestDto {
    @ApiModelProperty(example = "로그인 시 발급되었던 accessToken", value = "accessToken")
    private String accessToken;

    @ApiModelProperty(example = "로그인 시 발급되었던 refreshToken", value = "refreshToken")
    private String refreshToken;
}
