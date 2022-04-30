package com.study.shoestrade.dto.trade.request;

import com.study.shoestrade.domain.trade.TradeType;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeDto {
    @ApiModelProperty(example = "1", value = "입찰 id (등록시 key 사용 X)")
    private Long id;

    @ApiModelProperty(example = "100000", value = "등록할 입찰 가격(구매가, 판매가)")
    private int price;

    @ApiModelProperty(example = "1", value = "등록할 상품의 사이즈 id")
    private Long productSizeId;

    @ApiModelProperty(example = "SELL", value = "입찰 타입(SELL, PURCHASE)")
    private TradeType tradeType;
}
