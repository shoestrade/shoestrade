package com.study.shoestrade.dto.interest.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MyInterest {

    private Long interestId;
    private Long productId;
    private Long productSizeId;
    private String brand;
    private String productName;
    private int size;
    private int price;
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
