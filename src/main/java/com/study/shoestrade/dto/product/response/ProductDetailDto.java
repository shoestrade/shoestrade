package com.study.shoestrade.dto.product.response;

import com.querydsl.core.annotations.QueryProjection;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import com.study.shoestrade.dto.product.request.ProductSaveDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.stream.Collectors;


@NoArgsConstructor
@Getter
@SuperBuilder
public class ProductDetailDto extends ProductLoadDto {

    private int lastedPrice;
    private int imSalesPrice;

    @QueryProjection
    public ProductDetailDto(Product product, int lastedPrice, int imSalesPrice, int imPurchasePrice) {
        super(product.getId(), product.getKorName(), product.getEngName(), product.getCode(),
                product.getColor(), product.getReleasePrice(), imPurchasePrice, product.getInterest(), product.getBrand().getEngName(),
                product.getImageList().stream().map(ProductImage::getName).collect(Collectors.toList()));
        this.lastedPrice = lastedPrice;
        this.imSalesPrice = imSalesPrice;
    }
}
