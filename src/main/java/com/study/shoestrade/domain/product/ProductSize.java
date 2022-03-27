package com.study.shoestrade.domain.product;

import com.study.shoestrade.domain.interest.InterestProduct;
import com.study.shoestrade.domain.trade.Trade;
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
@SequenceGenerator(name = "PRODUCT_SIZE_SEQ_GENERATOR", sequenceName = "PRODUCT_SIZE_SEQ")
public class ProductSize {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_SIZE_SEQ_GENERATOR")
    @Column(name = "productSize_id")
    private Long id;

    private int size;

    @OneToMany(mappedBy = "productSize")
    private List<Trade> tradeList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productSize")
    private List<InterestProduct> interestProductList = new ArrayList<>();

    @Builder
    public ProductSize(Long id, int size, List<Trade> tradeList, Product product, List<InterestProduct> interestProductList) {
        this.id = id;
        this.size = size;
        this.tradeList = tradeList;
        this.product = product;
        this.interestProductList = interestProductList;
    }
}
