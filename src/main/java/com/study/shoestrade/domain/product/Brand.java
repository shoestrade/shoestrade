package com.study.shoestrade.domain.product;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "BRAND_SEQ_GENERATOR", sequenceName = "BRAND_SEQ")
public class Brand {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BRAND_SEQ_GENERATOR")
    @Column(name = "brand_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "brand")
    private List<Product> productList = new LinkedList<>();

    @Builder
    public Brand(Long id, String name, List<Product> productList) {
        this.id = id;
        this.name = name;
        this.productList = productList;
    }

    public void changeBrandName(String name){
        this.name = name;
    }

}
