package com.study.shoestrade.dto.product;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductImageAddDto {
    @ApiModelProperty(example = "[이미지1.png,이미지2.png,이미지3.png]", value = "이미지 이름 목록")
    List<String> imageNameList;
}
