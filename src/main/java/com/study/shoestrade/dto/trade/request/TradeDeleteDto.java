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
public class TradeDeleteDto {

    private Long id;

    private TradeType tradeType;
}
