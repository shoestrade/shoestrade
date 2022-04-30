package com.study.shoestrade.dto.address.response;

import com.study.shoestrade.dto.address.AddressDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "(baseAddressDto)기본배송지와 (addressDtoPage)주소목록")
public class AddressListResponseDto {

    private AddressDto baseAddressDto;

    private Page<AddressDto> addressDtoPage;
}
