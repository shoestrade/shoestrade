package com.study.shoestrade.dto.product.response;

import com.querydsl.core.annotations.QueryProjection;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(example = "1", value = "상품 id")
    private Long id;

    @ApiModelProperty(example = "에어 맥스 97", value = "상품 한글 이름")
    private String korName;

    @ApiModelProperty(example = "Air Max 97", value = "상품 영문 이름")
    private String engName;
    @ApiModelProperty(example = "cu1029-231", value = "상품 코드")
    private String code;
    @ApiModelProperty(example = "white", value = "상품 색상")
    private String color;
    @ApiModelProperty(example = "300000", value = "발매가")
    private int releasePrice;
    @ApiModelProperty(example = "400000", value = "즉시 구매가")
    private int imPurchasePrice;
    @ApiModelProperty(example = "10001", value = "관심 상품 등록 횟수")
    private int interest;
    @ApiModelProperty(example = "Nike", value = "브랜드 이름")
    private String brandName;
    @ApiModelProperty(example = "[이미지1.png,이미지2.png,이미지3.png]", value = "이미지 이름 목록")
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
