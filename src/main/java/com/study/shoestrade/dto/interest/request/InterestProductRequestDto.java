package com.study.shoestrade.dto.interest.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InterestProductRequestDto {
    @ApiModelProperty(example = "[1,2,3]", value = "관심 상품으로 등록할 상품 사이즈들의 id")
    private List<Long> interests;
}
