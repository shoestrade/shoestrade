package com.study.shoestrade.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductSearchDto {

    private String name;

    private List<Long> brandIdList;

    @Builder
    public ProductSearchDto(String name, List<Long> brandIdList) {
        this.name = name;
        this.brandIdList = brandIdList;
    }
}
