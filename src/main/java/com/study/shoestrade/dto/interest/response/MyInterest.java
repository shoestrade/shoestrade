package com.study.shoestrade.dto.interest.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MyInterest {
    @ApiModelProperty(example = "1", value = "관심 상품 id")
    private Long interestId;

    @ApiModelProperty(example = "1", value = "상품 id")
    private Long productId;

    @ApiModelProperty(example = "1", value = "상품 사이즈 id")
    private Long productSizeId;

    @ApiModelProperty(example = "Nike", value = "brand 이름")
    private String brand;

    @ApiModelProperty(example = "에어 맥스 97", value = "상품 이름")
    private String productName;

    @ApiModelProperty(example = "260", value = "상품 사이즈")
    private int size;

    @ApiModelProperty(example = "210000", value = "최소 구매 가격")
    private int price;

    @ApiModelProperty(example = "이미지1.png", value = "상품 이미지 이름")
    private String imgName;

    @QueryProjection
    public MyInterest(Long interestId, Long productId, Long productSizeId, String brand, String productName, int size, int price, String imgName) {
        this.interestId = interestId;
        this.productId = productId;
        this.productSizeId = productSizeId;
        this.brand = brand;
        this.productName = productName;
        this.size = size;
        this.price = price;
        this.imgName = imgName;
    }
}
