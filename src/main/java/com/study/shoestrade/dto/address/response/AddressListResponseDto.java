package com.study.shoestrade.dto.address.response;

import com.study.shoestrade.dto.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressListResponseDto {

    private AddressDto baseAddressDto;
    private Page<AddressDto> addressDtoPage;
}
