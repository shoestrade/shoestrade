package com.study.shoestrade.dto;

import com.study.shoestrade.domain.product.Brand;
import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.product.ProductImage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ProductLoadDto {

    private Long id;
    private String name;
    private String code;
    private String color;
    private int releasePrice;
    private int interest;
    private long brandId;
    private List<String> imageList = new ArrayList<>();

    @Builder
    public ProductLoadDto(Long id, String name, String code, String color, int releasePrice, int interest, long brandId, List<String> imageList) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.color = color;
        this.releasePrice = releasePrice;
        this.interest = interest;
        this.brandId = brandId;
        this.imageList = imageList;
    }

    public static ProductLoadDto create(Product product) {
        return ProductLoadDto.builder()
                .id(product.getId())
                .name(product.getName())
                .code(product.getCode())
                .color(product.getColor())
                .interest(product.getInterest())
                .releasePrice(product.getReleasePrice())
                .brandId(product.getBrand().getId())
                .imageList(product.getImageList().stream().map(ProductImage::getName).collect(Collectors.toList()))
                .build();
    }
}
