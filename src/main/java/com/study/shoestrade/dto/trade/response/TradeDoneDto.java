package com.study.shoestrade.dto.trade.response;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(example = "220", value = "신발 사이즈")
    int size;


    @ApiModelProperty(example = "120000", value = "거래가격")
    int price;

    @ApiModelProperty(example = "2022-03-22 12:31:23", value = "거래 완료 날짜")
    private LocalDateTime tradeDate;
}
