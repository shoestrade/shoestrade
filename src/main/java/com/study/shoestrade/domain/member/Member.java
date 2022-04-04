package com.study.shoestrade.domain.member;

import com.study.shoestrade.domain.interest.InterestProduct;
import com.study.shoestrade.domain.trade.Trade;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR", sequenceName = "MEMBER_SEQ")
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String phone;

    @Embedded
    private Address address;

    private int shoeSize;

    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.BRONZE;

    private int point;

    @Embedded
    private Account account;

    // Member role 추기
    @Embedded
    private Role role;

    @OneToMany(mappedBy = "seller")
    private List<Trade> sellList = new ArrayList<>();

    @OneToMany(mappedBy = "purchaser")
    private List<Trade> purchaseList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<InterestProduct> interestProductList = new ArrayList<>();

}
