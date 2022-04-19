package com.study.shoestrade.dto.product.response;

import com.querydsl.core.annotations.QueryProjection;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import com.study.shoestrade.dto.product.ProductDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.stream.Collectors;


@NoArgsConstructor
@SuperBuilder
public class ProductDetailDto extends ProductDto{

    private int lastedPrice;
    private int imSalesPrice;
    private int imPurchasePrice;

    @QueryProjection
    public ProductDetailDto(Product product, int lastedPrice, int imSalesPrice, int imPurchasePrice) {
        super(product.getId(), product.getKorName(), product.getEngName(), product.getCode(),
                product.getColor(), product.getReleasePrice(), product.getInterest(), product.getBrand().getId(),
                product.getImageList().stream().map(ProductImage::getName).collect(Collectors.toList()));
        this.lastedPrice = lastedPrice;
        this.imSalesPrice = imSalesPrice;
        this.imPurchasePrice = imPurchasePrice;
    }
}
