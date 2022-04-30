package com.study.shoestrade.dto.trade.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeTransactionDto {

    @ApiModelProperty(example = "250", value = "신발 사이즈")
    private int size;

    @ApiModelProperty(example = "12000", value = "거래가")
    private int price;

    @ApiModelProperty(example = "231", value = "사이즈별 수량")
    private Long quantity;
}
