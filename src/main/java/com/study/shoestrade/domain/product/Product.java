package com.study.shoestrade.domain.product;

import com.study.shoestrade.dto.ProductDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "PRODUCT_SEQ_GENERATOR", sequenceName = "PRODUCT_SEQ")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_SEQ_GENERATOR")
    @Column(name = "product_id")
    private Long id;

    private String name;
    private String code;
    private String color;
    private int releasePrice;
    private int interest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany
    @JoinColumn(name = "product_id")
    private List<ProductImage> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductSize> productSizeList = new ArrayList<>();

    @Builder
    public Product(Long id, String name, String code, String color, int releasePrice, int interest, Brand brand, List<ProductImage> imageList, List<ProductSize> productSizeList) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.color = color;
        this.releasePrice = releasePrice;
        this.interest = interest;
        this.brand = brand;
        this.imageList = imageList;
        this.productSizeList = productSizeList;
    }

    public void changeProduct(ProductDto productDto) {
        this.id = productDto.getId();
        this.name = productDto.getName();
        this.code = productDto.getCode();
        this.color = productDto.getColor();
        this.releasePrice = productDto.getReleasePrice();
        this.interest = productDto.getInterest();
    }

    public void changeProductBrand(Brand brand) {
        this.brand = brand;
    }
}
