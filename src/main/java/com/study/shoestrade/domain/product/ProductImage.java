package com.study.shoestrade.domain.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "PRODUCT_IMAGE_SEQ_GENERATOR", sequenceName = "PRODUCT_IMAGE_SEQ")
public class ProductImage {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_IMAGE_SEQ_GENERATOR")
    @Column(name = "productImage_id" )
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public ProductImage(Long id, String name, Product product) {
        this.id = id;
        this.name = name;
        this.product = product;
    }
}
