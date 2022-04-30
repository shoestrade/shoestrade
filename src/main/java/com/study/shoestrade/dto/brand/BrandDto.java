package com.study.shoestrade.dto.brand;

import com.study.shoestrade.domain.product.Brand;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDto {

    @ApiModelProperty(value = "브랜드 id (등록, 수정시 해당 key 사용 X)", example = "1")
    private Long id;

    @ApiModelProperty(example = "나이키", value = "브랜드 한글 이름")
    private String korName;

    @ApiModelProperty(example = "Nike", value = "브랜드 영문 이름")
    private String engName;

    public static BrandDto create(Brand brand) {
        return BrandDto.builder()
                .id(brand.getId())
                .engName(brand.getEngName())
                .korName(brand.getKorName())
                .build();

    }
}
