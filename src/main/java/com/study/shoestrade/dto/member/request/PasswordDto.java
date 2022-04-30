package com.study.shoestrade.dto.member.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PasswordDto {
    @ApiModelProperty(example = "1234123b",value = "이전 비밀번호")
    private String prePassword;

    @ApiModelProperty(example = "1234123a",value = "변경할 비밀번호")
    private String newPassword;
}
