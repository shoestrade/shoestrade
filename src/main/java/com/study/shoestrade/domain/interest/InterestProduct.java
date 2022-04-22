package com.study.shoestrade.domain.interest;

import com.study.shoestrade.domain.BaseEntity;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.product.ProductSize;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InterestProduct extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interestProduct_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productSize_id")
    private ProductSize productSize;

}
