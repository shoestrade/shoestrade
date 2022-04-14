package com.study.shoestrade.dto.address;

import com.study.shoestrade.domain.member.Address;
import com.study.shoestrade.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    private Long id;
    private String name;
    private String phone;
    private String addressName; // 주소
    private String detail; // 상세 주소
    private String zipcode; // 우편번호
    private boolean baseAddress;

    public boolean getBaseAddress(){
        return baseAddress;
    }

    public Address toEntity(Member member, boolean baseAddress){
        return Address.builder()
                .id(id)
                .name(name)
                .phone(phone)
                .addressName(addressName)
                .detail(detail)
                .zipcode(zipcode)
                .baseAddress(baseAddress)
                .member(member)
                .build();
    }
}