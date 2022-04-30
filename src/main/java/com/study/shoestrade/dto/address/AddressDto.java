package com.study.shoestrade.dto.address;

import com.study.shoestrade.domain.member.Address;
import com.study.shoestrade.domain.member.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    @ApiModelProperty(example = "1", value = "주소 id (등록시 key 사용 X)")
    private Long id;

    @ApiModelProperty(example = "우리집", value = "주소 이름")
    private String name;

    @ApiModelProperty(example = "010-1234-1234", value = "휴대폰 번호")
    private String phone;

    @ApiModelProperty(example = "광주 우산로 107번길 67", value = "주소")
    private String addressName; // 주소

    @ApiModelProperty(example = "1111동 1231호", value = "상세 주소")
    private String detail; // 상세 주소

    @ApiModelProperty(example = "62365", value = "우편번호")
    private String zipcode; // 우편번호

    @ApiModelProperty(example = "true", value = "기본 배송지 여부")
    private boolean baseAddress;

    public boolean getBaseAddress() {
        return baseAddress;
    }

    public Address toEntity(Member member, boolean baseAddress) {
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
