package com.study.shoestrade.dto.product;

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
public class ProductImageAddDto {

    @ApiModelProperty(example = "1", value = "상품 id")
    Long productId;

    @ApiModelProperty(example = "[이미지1.png,이미지2.png,이미지3.png]", value = "이미지 이름 목록")
    List<String> imageNameList;
}
