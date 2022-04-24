package com.study.shoestrade.dto.trade.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeDoneDto {

    private int size;
    private int price;
    private LocalDateTime tradeDate;
}
