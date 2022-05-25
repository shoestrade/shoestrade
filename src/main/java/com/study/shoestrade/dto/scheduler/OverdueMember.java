package com.study.shoestrade.dto.scheduler;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OverdueMember {
    private Long memberId;
    private Long count;
}
