package com.study.shoestrade.dto.trade.response;

import com.querydsl.core.annotations.QueryProjection;
import com.study.shoestrade.domain.trade.TradeState;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TradeLoadDto {

    @ApiModelProperty(example = "1", value = "입찰 id")
    private Long id;

    @ApiModelProperty(example = "에어 맥스 98", value = "상품 한글 이름")
    private String productKorName;

    @ApiModelProperty(example = "250", value = "신발 사이즈")
    private int size;

    @ApiModelProperty(example = "100000", value = "(구매, 판매, 완료)입찰 가격")
    private int price;

    @ApiModelProperty(example = "2022-03-22 12:31:23", value = "거래 완료 날짜")
    private LocalDateTime tradeCompletionDate;

    @ApiModelProperty(example = "SELL", value = "거래 진행 상태")
    private TradeState tradeState;

    @ApiModelProperty(example = "이미지1.png", value = "상품 이미지 이름")
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
