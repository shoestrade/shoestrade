package com.study.shoestrade.dto.product.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchDto {

    @ApiModelProperty(example = "나이", value = "검색어(상품 이름)")
    private String name;

    @ApiModelProperty(example = "[1,2]", value = "브랜드 id 리스트")
    private List<Long> brandIdList;
}
