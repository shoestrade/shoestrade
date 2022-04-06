package com.study.shoestrade.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductImageAddDto {

    Long id;

    List<ProductImageDto> imageList;

    @Builder
    public ProductImageAddDto(Long id, List<ProductImageDto> imageList) {
        this.id = id;
        this.imageList = imageList;
    }
}
