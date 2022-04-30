package com.study.shoestrade.dto.interest.response;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ApiModel(description = "사이즈별 관심 상품 등록 여부")
public class InterestProductResponseDto {
    private List<InterestProductResponseSizeDto> interestProductSizes;
}
