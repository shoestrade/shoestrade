package com.study.shoestrade.dto.product;

import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDto {

    Long id;
    String name;

    public ProductImage toEntity(Product product) {
        return ProductImage.builder()
                .id(this.id)
                .name(this.name)
                .product(product)
                .build();
    }
}
