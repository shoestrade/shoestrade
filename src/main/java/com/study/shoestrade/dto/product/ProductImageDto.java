package com.study.shoestrade.dto.product;

import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductImageDto {

    @ApiModelProperty(example = "1", value = "이미지 id")
    Long id;

    @ApiModelProperty(example = "이미지.png", value = "이미지 이름")
    String name;

    public static ProductImageDto create(ProductImage productImage) {
        return ProductImageDto.builder()
                .id(productImage.getId())
                .name(productImage.getName())
                .build();

    }

    public ProductImage toEntity(Product product) {
        return ProductImage.builder()
                .name(this.name)
                .id(this.id)
                .product(product)
                .build();
    }
}
