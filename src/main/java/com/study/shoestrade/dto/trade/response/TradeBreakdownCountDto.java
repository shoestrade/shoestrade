package com.study.shoestrade.dto.trade.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class TradeBreakdownCountDto {
    @ApiModelProperty(example = "3", value = "(구매, 판매)입찰 갯수")
    private Long bidCount;

    @ApiModelProperty(example = "11", value = "(구매, 판매)진행 중 갯수")
    private Long progressCount;

    @ApiModelProperty(example = "113", value = "(구매, 판매)종료 갯수")
    private Long doneCount;

    @QueryProjection
    public TradeBreakdownCountDto(Long bidCount, Long progressCount, Long doneCount) {
        this.bidCount = bidCount;
        this.progressCount = progressCount;
        this.doneCount = doneCount;
    }
}
