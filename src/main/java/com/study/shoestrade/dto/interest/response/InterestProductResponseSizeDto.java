package com.study.shoestrade.dto.interest.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InterestProductResponseSizeDto {
    @ApiModelProperty(example = "1", value = "상품 사이즈 id")
    private Long productSizeId;

    @ApiModelProperty(example = "true", value = "관심 상품 등록 여부")
    private boolean checked;
}
