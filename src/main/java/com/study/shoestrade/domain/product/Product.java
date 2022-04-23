package com.study.shoestrade.domain.product;

import com.study.shoestrade.domain.BaseEntity;
import com.study.shoestrade.dto.product.request.ProductSaveDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String korName;

    private String engName;

    private String code;
    private String color;
    private int releasePrice;
    private int interest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductSize> productSizeList = new ArrayList<>();

    public void changeProduct(ProductSaveDto productDto) {
        this.id = productDto.getId();
        this.korName = productDto.getKorName();
        this.engName = productDto.getEngName();
        this.code = productDto.getCode();
        this.color = productDto.getColor();
        this.releasePrice = productDto.getReleasePrice();
        this.interest = productDto.getInterest();
    }

    public void changeProductBrand(Brand brand) {
        this.brand = brand;
    }
}
