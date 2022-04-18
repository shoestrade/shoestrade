package com.study.shoestrade.domain.member;

import com.study.shoestrade.domain.interest.InterestProduct;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.dto.account.AccountDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String phone;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Address> addressList = new ArrayList<>();

    private int shoeSize;

    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.BRONZE;

    private int point;

    @Embedded
    private Account account;

    // Member role 추가
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_MEMBER;

    // 정지 해제 시간
    private LocalDateTime banReleaseTime;

    @OneToMany(mappedBy = "seller")
    private List<Trade> sellList = new ArrayList<>();

    @OneToMany(mappedBy = "purchaser")
    private List<Trade> purchaseList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<InterestProduct> interestProductList = new ArrayList<>();

    public void changePassword(String password){
        this.password = password;
    }

    public void addAddress(Address address){
        this.addressList.add(address);
    }

    public Account changeAccount(AccountDto accountDto){
        Account account = accountDto.toEntity();
        this.account = account;
        return this.account;
    }

    public void deleteAccount(){
        this.account = null;
    }

    public void changePhone(String number){
        this.phone = number;
    }

    public void changeShoeSize(int size){
        shoeSize = size;
    }

    public void updateBanReleaseTime(LocalDateTime time){
        this.banReleaseTime = time;
    }

    public void changeRole(Role role){
        this.role = role;
    }
}
