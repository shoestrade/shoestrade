package com.study.shoestrade.dto.trade.response;

import com.querydsl.core.annotations.QueryProjection;
import com.study.shoestrade.domain.trade.TradeState;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TradeLoadDto {

    private Long id;
    private String productKorName;

    private int size;
    private int price;
    private LocalDateTime tradeCompletionDate;
    private TradeState tradeState;
    private String image;

    @Builder
    @QueryProjection
    public TradeLoadDto(Long id, String productKorName, int size, int price, LocalDateTime tradeCompletionDate, TradeState tradeState, String image) {
        this.id = id;
        this.productKorName = productKorName;
        this.size = size;
        this.price = price;
        this.tradeCompletionDate = tradeCompletionDate;
        this.tradeState = tradeState;
        this.image = image;
    }

    @QueryProjection
    public TradeLoadDto(Long id, int size, int price) {
        this.id = id;
        this.size = size;
        this.price = price;
    }
}
