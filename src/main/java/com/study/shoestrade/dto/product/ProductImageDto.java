package com.study.shoestrade.dto.product;

import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDto {

    @ApiModelProperty(example = "1", value = "이미지 id")
    Long id;

    @ApiModelProperty(example = "이미지.png", value = "이미지 이름")
    String name;

    public ProductImage toEntity(Product product) {
        return ProductImage.builder()
                .id(this.id)
                .name(this.name)
                .product(product)
                .build();
    }
}
