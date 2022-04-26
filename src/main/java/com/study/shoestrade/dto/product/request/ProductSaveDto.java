package com.study.shoestrade.dto.product.request;

import com.study.shoestrade.domain.product.Brand;
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
public class ProductSaveDto {

    private Long id;
    private String korName;

    private String engName;
    private String code;
    private String color;
    private int releasePrice;
    private int interest;
    private Long brandId;
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