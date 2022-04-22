package com.study.shoestrade.dto.interest.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InterestProductResponseDto {
    private List<InterestProductResponseSizeDto> interestProductSizes;
}
