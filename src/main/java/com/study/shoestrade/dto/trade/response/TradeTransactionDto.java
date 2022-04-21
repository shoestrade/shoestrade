package com.study.shoestrade.dto.trade.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeTransactionDto {

    private int size;
    private int price;
    private Long quantity;
}
