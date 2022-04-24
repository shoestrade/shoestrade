package com.study.shoestrade.dto.trade.request;

import com.study.shoestrade.domain.trade.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeDto {
    private Long id;

    private int price;

    private Long productSizeId;

    private TradeType tradeType;
}
