package com.study.shoestrade.domain.product;

import com.study.shoestrade.domain.BaseEntity;
import com.study.shoestrade.domain.interest.InterestProduct;
import com.study.shoestrade.domain.trade.Trade;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productSize_id")
    private Long id;

    private int size;

    @OneToMany(mappedBy = "productSize")
    private List<Trade> tradeList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "productSize", cascade = CascadeType.ALL)
    private List<InterestProduct> interestProductList = new ArrayList<>();

}
