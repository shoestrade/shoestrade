package com.study.shoestrade.dto.interest.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InterestProductResponseSizeDto {
    private Long productSizeId;
    private boolean checked;
}
