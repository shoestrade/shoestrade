package com.study.shoestrade.dto.product.response;

import com.querydsl.core.annotations.QueryProjection;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductLoadDto {

    private Long id;
    private String korName;

    private String engName;
    private String code;
    private String color;
    private int releasePrice;
    private int imPurchasePrice;
    private int interest;
    private String brandName;
    private List<String> imageList = new ArrayList<>();

    @QueryProjection
    public ProductLoadDto(Product product, int imPurchasePrice) {
        this.id = product.getId();
        this.korName = product.getKorName();
        this.engName = product.getEngName();
        this.code = product.getCode();
        this.color = product.getColor();
        this.releasePrice = product.getReleasePrice();
        this.imPurchasePrice = imPurchasePrice;
        this.interest = product.getInterest();
        this.brandName = product.getBrand().getEngName();
        this.imageList = product.getImageList().stream().map(ProductImage::getName).collect(Collectors.toList());
    }
}
