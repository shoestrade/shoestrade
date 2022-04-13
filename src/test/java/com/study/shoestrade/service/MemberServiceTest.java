package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Address;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.address.AddressDto;
import com.study.shoestrade.dto.address.response.AddressListResponseDto;
import com.study.shoestrade.exception.address.AddressNotFoundException;
import com.study.shoestrade.exception.address.BaseAddressNotDeleteException;
import com.study.shoestrade.exception.address.BaseAddressUncheckedException;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.repository.member.AddressRepository;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    AddressRepository addressRepository;

    @Test
    @DisplayName("주소를 처음 등록하면 기본 주소로 등록된다.")
    public void 주소_등록_성공1() {
        // given
        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .addressList(new ArrayList<>())
                .build();

        AddressDto requestDto = AddressDto.builder()
                .name("syt")
                .phone("01011112222")
                .addressName("왕버들로 132")
                .detail("203-1004")
                .baseAddress(false)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        // when
        AddressDto responseDto = memberService.addAddress(member.getEmail(), requestDto);

        // then
        assertThat(member.getAddressList().size()).isEqualTo(1);
        assertThat(responseDto.getBaseAddress()).isTrue();
    }

    @Test
    @DisplayName("기본 주소가 등록되어 있고 주소를 등록하면 기본 주소로 등록되지 않는다.")
    public void 주소_등록_성공2() {
        // given
        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(Address.builder()
                .name("asd")
                .phone("010121213123")
                .baseAddress(true)
                .build());

        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .addressList(addresses)
                .build();

        AddressDto requestDto = AddressDto.builder()
                .name("syt")
                .phone("01011112222")
                .addressName("왕버들로 132")
                .detail("203-1004")
                .baseAddress(false)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        // when
        AddressDto responseDto = memberService.addAddress(member.getEmail(), requestDto);

        // then
        assertThat(member.getAddressList().size()).isEqualTo(2);
        assertThat(responseDto.getBaseAddress()).isFalse();
    }

    @Test
    @DisplayName("기본 주소가 등록되어 있는 상태에서 새로운 기본 주소를 등록하면 기본 주소가 변경된다.")
    public void 주소_등록_성공3() {
        // given
        ArrayList<Address> addresses = new ArrayList<>();
        Address address1 = Address.builder()
                .name("asd")
                .phone("010121213123")
                .baseAddress(true)
                .build();
        addresses.add(address1);

        Member member = Member.builder()
                .id(1L)
                .email("tt@g.com")
                .password("PW")
                .addressList(addresses)
                .build();

        AddressDto requestDto = AddressDto.builder()
                .name("syt")
                .phone("01011112222")
                .addressName("왕버들로 132")
                .detail("203-1004")
                .baseAddress(true)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
        given(addressRepository.findBaseAddress(any())).willReturn(Optional.of(address1));

        // when
        AddressDto responseDto = memberService.addAddress(member.getEmail(), requestDto);

        // then
        assertThat(member.getAddressList().size()).isEqualTo(2);
        assertThat(responseDto.getBaseAddress()).isTrue();
    }

    @Test
    @DisplayName("회원이 일치하지 않으면 주소 등록에 실패한다.")
    public void 주소_등록_실패() {
        // given
        AddressDto requestDto = AddressDto.builder()
                .name("syt")
                .phone("01011112222")
                .addressName("왕버들로 132")
                .detail("203-1004")
                .baseAddress(false)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.addAddress("asd", requestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }
    
    @Test
    public void 주소_목록() {
        // given
        ArrayList<Address> addresses = new ArrayList<>();
        Address address1 = Address.builder()
                .name("aaa")
                .phone("01011111111")
                .baseAddress(false)
                .build();
        Address address2 = Address.builder()
                .name("bbb")
                .phone("01022222222")
                .baseAddress(false)
                .build();

        addresses.add(address2);

        Pageable pageable = PageRequest.of(0, 1);
        Page<Address> page = new PageImpl<>(addresses, pageable, 1L);

        // mocking
        given(addressRepository.findBaseAddress(any())).willReturn(Optional.ofNullable(address1));
        given(addressRepository.findAddressList(any(), any())).willReturn(page);

        // when
        AddressListResponseDto addressList = memberService.getAddressList("email", pageable);

        // then
        assertThat(addressList.getAddressDtoPage().getSize()).isEqualTo(1);
        assertThat(addressList.getBaseAddressDto().getPhone()).isEqualTo("01011111111");
    }

    @Test
    @DisplayName("기본 주소를 변경하면 기존의 기본 주소와 변경된다.")
    public void 기본주소_변경_성공() {
        // given
        Address preBaseAddress = Address.builder()
                .id(1L)
                .name("aaa")
                .phone("01011111111")
                .baseAddress(true)
                .build();

        Address newBaseAddress = Address.builder()
                .id(2L)
                .name("bbb")
                .phone("01022222222")
                .baseAddress(false)
                .build();

        // given
        given(addressRepository.findById(any())).willReturn(Optional.of(newBaseAddress));
        given(addressRepository.findBaseAddress(any())).willReturn(Optional.of(preBaseAddress));

        // when
        memberService.changeBaseAddress("email", 1L);

        // then
        assertThat(preBaseAddress.getBaseAddress()).isFalse();
        assertThat(newBaseAddress.getBaseAddress()).isTrue();
    }

    @Test
    @DisplayName("주소를 수정한다.")
    public void 주소_수정_성공1() {
        // given
        Address address1 = Address.builder()
                .id(1L)
                .name("aaa")
                .phone("01011111111")
                .baseAddress(true)
                .build();

        Address address2 = Address.builder()
                .id(2L)
                .name("bbb")
                .phone("01022222222")
                .baseAddress(false)
                .build();

        AddressDto updateAddress = AddressDto.builder()
                .name("ccc")
                .phone("01033333333")
                .baseAddress(false)
                .build();

        // given
        given(addressRepository.findById(any())).willReturn(Optional.of(address2));

        // when
        AddressDto newAddress = memberService.updateAddress("email",2L, updateAddress);

        // then
        assertThat(newAddress.getName()).isEqualTo("ccc");
    }

    @Test
    @DisplayName("기본 주소가 아닌 주소를 기본 주소로 수정한다.")
    public void 주소_수정_성공2() {
        Address address1 = Address.builder()
                .id(1L)
                .name("aaa")
                .phone("01011111111")
                .baseAddress(true)
                .build();

        Address address2 = Address.builder()
                .id(2L)
                .name("bbb")
                .phone("01022222222")
                .baseAddress(false)
                .build();

        AddressDto updateAddress = AddressDto.builder()
                .name("ccc")
                .phone("01033333333")
                .baseAddress(true)
                .build();

        // given
        given(addressRepository.findById(any())).willReturn(Optional.of(address2));
        given(addressRepository.findBaseAddress(any())).willReturn(Optional.of(address1));

        // when
        AddressDto newAddress = memberService.updateAddress("email", 2L, updateAddress);

        // then
        assertThat(newAddress.getName()).isEqualTo("ccc");
        assertThat(newAddress.getBaseAddress()).isTrue();
        assertThat(address1.getBaseAddress()).isFalse();
    }

    @Test
    @DisplayName("로그인 되어있지 않으면 기본 주소를 찾을 수 없다.")
    public void 주소_수정_실패1() {
        // given
        Address address1 = Address.builder()
                .id(1L)
                .name("aaa")
                .phone("01011111111")
                .baseAddress(true)
                .build();

        AddressDto updateAddress = AddressDto.builder()
                .name("ccc")
                .phone("01033333333")
                .baseAddress(true)
                .build();

        // given
        given(addressRepository.findById(any())).willReturn(Optional.of(address1));
        given(addressRepository.findBaseAddress(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.updateAddress("email", 1L, updateAddress))
                .isInstanceOf(AddressNotFoundException.class);
    }

    @Test
    @DisplayName("기본 주소를 해제하면 BaseAddressUncheckedException 예외가 발생한다.")
    public void 주소_수정_실패2() {
        // given
        Address address1 = Address.builder()
                .id(1L)
                .name("aaa")
                .phone("01011111111")
                .baseAddress(true)
                .build();

        AddressDto updateAddress = AddressDto.builder()
                .name("ccc")
                .phone("01033333333")
                .baseAddress(false)
                .build();

        // given
        given(addressRepository.findById(any())).willReturn(Optional.of(address1));

        // when, then
        assertThatThrownBy(() -> memberService.updateAddress("email", 1L, updateAddress))
                .isInstanceOf(BaseAddressUncheckedException.class);
    }

    @Test
    @DisplayName("기본 주소가 아니라면 주소를 삭제할 수 있다.")
    public void 주소_삭제_성공() {
        // given
        Address address1 = Address.builder()
                .id(1L)
                .name("aaa")
                .phone("01011111111")
                .baseAddress(false)
                .build();

        // given
        given(addressRepository.findById(any())).willReturn(Optional.of(address1));

        // when
        AddressDto addressDto = memberService.deleteAddress(1L);

        // then
        assertThat(addressDto.getName()).isEqualTo("aaa");
    }

    @Test
    @DisplayName("기본 주소는 삭제할 수 없다.")
    public void 주소_삭제_실패() {
        // given
        Address address1 = Address.builder()
                .id(1L)
                .name("aaa")
                .phone("01011111111")
                .baseAddress(true)
                .build();

        // given
        given(addressRepository.findById(any())).willReturn(Optional.of(address1));

        // when, then
        assertThatThrownBy(() -> memberService.deleteAddress(1L))
                .isInstanceOf(BaseAddressNotDeleteException.class);
    }
}