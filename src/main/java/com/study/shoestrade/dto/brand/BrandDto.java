package com.study.shoestrade.dto.brand;

import com.study.shoestrade.domain.product.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDto {

    private Long id;

    private String korName;

    private String engName;

    public static BrandDto create(Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .engName(brand.getEngName())
                .korName(brand.getKorName())
                .build();

    }
}
