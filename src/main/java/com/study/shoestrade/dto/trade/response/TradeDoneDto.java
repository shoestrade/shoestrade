package com.study.shoestrade.dto.trade.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDoneDto {

    private int Size;
    private int price;
    private LocalDateTime tradeDate;
}
