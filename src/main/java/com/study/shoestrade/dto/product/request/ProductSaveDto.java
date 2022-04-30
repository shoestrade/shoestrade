package com.study.shoestrade.dto.product.request;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductSaveDto {
    @ApiModelProperty(example = "해당 key 사용 X", value = "상품의 id")
    private Long id;
    @ApiModelProperty(example = "나이키 에어 맥스 97", value = "상품의 한글 이름")
    private String korName;
    @ApiModelProperty(example = "Nike Air Max 97", value = "상품의 영문 이름")
    private String engName;
    @ApiModelProperty(example = "cu1011-229", value = "상품의 코드")
    private String code;
    @ApiModelProperty(example = "white/grey", value = "상품의 색상")
    private String color;
    @ApiModelProperty(example = "219000", value = "상품의 발매가")
    private int releasePrice;
    @ApiModelProperty(example = "142", value = "상품의 총 관심 상품 등록 횟수 (해당 key 사용 X)")
    private int interest;

    @ApiModelProperty(example = "1", value = "상품의 브랜드 id")
    private Long brandId;

    @ApiModelProperty(example = "[이미지 이름1.png, 이미지 이름2.png]", value = "상품의 이미지 이름 리스트")
    private List<String> imageList = new ArrayList<>();


    public Product toEntity(Brand brand) {
        return Product.builder()
                .id(this.id)
                .korName(this.korName)
                .engName(this.engName)
                .code(this.code)
                .color(this.color)
                .releasePrice(this.releasePrice)
                .interest(this.interest)
                .imageList(new ArrayList<>())
                .brand(brand)
                .build();
    }
}
