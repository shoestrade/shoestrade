package com.study.shoestrade.dto.product;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductImageAddDto {

    Long productId;

    List<String> imageNameList;

    @Builder
    public ProductImageAddDto(Long productId, List<String> imageNameList) {
        this.productId = productId;
        this.imageNameList = imageNameList;
    }
}
