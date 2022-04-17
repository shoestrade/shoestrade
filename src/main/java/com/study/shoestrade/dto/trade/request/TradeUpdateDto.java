package com.study.shoestrade.dto.trade.request;

import com.querydsl.core.annotations.QueryProjection;
import com.study.shoestrade.domain.trade.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeUpdateDto {

    private Long id;

    private int price;

    private TradeType tradeType;
}
