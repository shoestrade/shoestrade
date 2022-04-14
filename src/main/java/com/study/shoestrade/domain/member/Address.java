package com.study.shoestrade.domain.member;

import com.study.shoestrade.domain.BaseEntity;
import com.study.shoestrade.dto.address.AddressDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Address extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Long id;

    private String name;
    private String phone;
    private String addressName; // 주소
    private String detail; // 상세 주소
    private String zipcode; // 우편번호
    private boolean baseAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    public AddressDto toAddressDto(){
        return AddressDto.builder()
                .id(id)
                .name(name)
                .phone(phone)
                .addressName(addressName)
                .detail(detail)
                .zipcode(zipcode)
                .baseAddress(baseAddress)
                .build();
    }

    public boolean getBaseAddress(){
        return baseAddress;
    }

    public void updateAddress(AddressDto addressDto){
        name = addressDto.getName();
        phone = addressDto.getPhone();
        addressName = addressDto.getAddressName();
        detail = addressDto.getDetail();
        zipcode = addressDto.getZipcode();
        baseAddress = addressDto.getBaseAddress();
    }

    public void changeBaseAddress(boolean isBaseAddress){
        baseAddress = isBaseAddress;
    }
}
