package com.study.shoestrade.dto.admin;

import com.study.shoestrade.domain.interest.InterestProduct;
import com.study.shoestrade.domain.member.*;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.dto.address.AddressDto;
import com.study.shoestrade.dto.interest.response.MyInterest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberDetailDto {

    private String email;
    private String name;
    private String phone;
    private List<AddressDto> addressList;
    private int shoeSize;
    private Grade grade;
    private int point;
    private Account account;
    private Role role;
    private LocalDateTime banReleaseTime;
//    private List<Trade> sellList;
//    private List<Trade> purchaseList;
    private List<MyInterest> interestProductList;

    public static MemberDetailDto create(Member member, List<MyInterest> interestProductList){
        return MemberDetailDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .addressList(member.getAddressList().stream().map(Address::toAddressDto).collect(Collectors.toList()))
                .shoeSize(member.getShoeSize())
                .grade(member.getGrade())
                .point(member.getPoint())
                .account(member.getAccount())
                .role(member.getRole())
                .banReleaseTime(member.getBanReleaseTime())
//                .sellList(member.getSellList())
//                .purchaseList(member.getPurchaseList())
                .interestProductList(interestProductList)
                .build();
    }
}
