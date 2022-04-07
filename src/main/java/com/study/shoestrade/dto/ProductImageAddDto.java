package com.study.shoestrade.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductImageAddDto {

    Long id;

    List<String> imageNameList;

    @Builder
    public ProductImageAddDto(Long id, List<String> imageNameList) {
        this.id = id;
        this.imageNameList = imageNameList;
    }
}
