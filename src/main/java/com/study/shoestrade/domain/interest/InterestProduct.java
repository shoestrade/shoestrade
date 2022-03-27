package com.study.shoestrade.domain.interest;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.product.ProductSize;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "INTEREST_PRODUCT_SEQ_GENERATOR", sequenceName = "INTEREST_PRODUCT_SEQ")
public class InterestProduct {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INTEREST_PRODUCT_SEQ_GENERATOR")
    @Column(name = "interestProduct_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productSize_id")
    private ProductSize productSize;

    @Builder
    public InterestProduct(Long id, Member member, ProductSize productSize) {
        this.id = id;
        this.member = member;
        this.productSize = productSize;
    }
}
