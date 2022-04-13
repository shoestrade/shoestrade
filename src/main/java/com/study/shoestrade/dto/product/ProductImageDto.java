package com.study.shoestrade.dto.product;

import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductImageDto {

    Long id;
    String name;

    @Builder
    public ProductImageDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductImage toEntity(Product product) {
        return ProductImage.builder()
                .id(this.id)
                .name(this.name)
                .product(product)
                .build();
    }
}
