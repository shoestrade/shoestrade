package com.study.shoestrade.domain.member;

import com.study.shoestrade.domain.trade.Trade;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Column(unique = true)
    private String phone;

    @Embedded
    private Address address;

    private int shoeSize;

    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.BRONZE;

    private int point;

    @Embedded
    private Account account;

    @OneToMany(mappedBy = "seller")
    private List<Trade> sellList = new ArrayList<>();

    @OneToMany(mappedBy = "purchaser")
    private List<Trade> purchaseList = new ArrayList<>();


    @Builder
    public Member(Long id, String email, String password, String name, String phone, Address address, int shoeSize, Grade grade, int point, Account account, List<Trade> sellList, List<Trade> purchaseList) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.shoeSize = shoeSize;
        this.grade = grade;
        this.point = point;
        this.account = account;
        this.sellList = sellList;
        this.purchaseList = purchaseList;
    }
}
