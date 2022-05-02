package com.study.shoestrade.dto.trade.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ApiModelProperty(example = "2022-03-22T12:31:23", value = "거래 완료 날짜")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime tradeDate;
}
