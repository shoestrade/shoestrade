package com.study.shoestrade.dto.member.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PointDto {

    @ApiModelProperty(example = "1000", value = "포인트")
    int point;
}
