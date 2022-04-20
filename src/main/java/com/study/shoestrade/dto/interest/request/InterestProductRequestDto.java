package com.study.shoestrade.dto.interest.request;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InterestProductRequestDto {
    private List<Long> interests;
}
