package com.study.shoestrade.dto.trade.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TradeLoadDto {

    private Long id;
    private String name;
    private int price;

    @Builder
    @QueryProjection
    public TradeLoadDto(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
