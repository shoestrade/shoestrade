package com.study.shoestrade.dto.product.response;

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
    private int interest;
    private String brandName;
    private List<String> imageList = new ArrayList<>();

    public static ProductLoadDto create(Product product) {
        return ProductLoadDto.builder()
                .id(product.getId())
                .korName(product.getKorName())
                .engName(product.getEngName())
                .code(product.getCode())
                .color(product.getColor())
                .interest(product.getInterest())
                .releasePrice(product.getReleasePrice())
                .brandName(product.getBrand().getEngName())
                .imageList(product.getImageList().stream().map(ProductImage::getName).collect(Collectors.toList()))
                .build();
    }
}
