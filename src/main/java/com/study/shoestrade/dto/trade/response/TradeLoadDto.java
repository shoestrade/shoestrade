package com.study.shoestrade.dto.trade.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class TradeLoadDto {

    private Long id;
    private String name;
    private int price;

    @QueryProjection
    public TradeLoadDto(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
