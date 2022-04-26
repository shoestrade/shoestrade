package com.study.shoestrade.dto.trade.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TradeBreakdownCountDto {
    private Long bidCount;
    private Long progressCount;
    private Long doneCount;

    @QueryProjection
    public TradeBreakdownCountDto(Long bidCount, Long progressCount, Long doneCount) {
        this.bidCount = bidCount;
        this.progressCount = progressCount;
        this.doneCount = doneCount;
    }
}
