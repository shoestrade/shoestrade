package com.study.shoestrade.dto;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String code;
    private String color;
    private int releasePrice;
    private int interest;
    private long brandId;

    @Builder
    public ProductDto(Long id, String name, String code, String color, int releasePrice, int interest, long brandId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.color = color;
        this.releasePrice = releasePrice;
        this.interest = interest;
        this.brandId = brandId;
    }

    public static ProductDto create(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .code(product.getCode())
                .color(product.getColor())
                .interest(product.getInterest())
                .releasePrice(product.getReleasePrice())
                .brandId(product.getBrand().getId())
                .build();
    }


    public Product toEntity(Brand brand) {
        return Product.builder()
                .id(this.id)
                .name(this.name)
                .code(this.code)
                .color(this.color)
                .releasePrice(this.releasePrice)
                .interest(this.interest)
                .brand(brand)
                .build();
    }
}
