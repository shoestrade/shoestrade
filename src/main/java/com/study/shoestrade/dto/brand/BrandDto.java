package com.study.shoestrade.dto.brand;

import com.study.shoestrade.domain.product.Brand;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrandDto {

    private Long id;

    private String name;

    @Builder
    public BrandDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static BrandDto create(Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();

    }

    public Brand toEntity() {
        return Brand.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}
