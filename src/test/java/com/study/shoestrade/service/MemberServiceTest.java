package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Account;
import com.study.shoestrade.domain.member.Address;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.account.AccountDto;
import com.study.shoestrade.dto.address.AddressDto;
import com.study.shoestrade.dto.address.response.AddressListResponseDto;
import com.study.shoestrade.dto.member.request.PasswordDto;
import com.study.shoestrade.dto.member.response.MemberDto;
import com.study.shoestrade.dto.member.response.PointDto;
import com.study.shoestrade.exception.address.AddressNotFoundException;
import com.study.shoestrade.exception.address.BaseAddressNotDeleteException;
import com.study.shoestrade.exception.address.BaseAddressUncheckedException;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.member.WrongPasswordException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("????????? ?????? ???????????? ?????? ????????? ????????????.")
    public void ??????_??????_??????1() {
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
                .addressName("???????????? 132")
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
    @DisplayName("?????? ????????? ???????????? ?????? ????????? ???????????? ?????? ????????? ???????????? ?????????.")
    public void ??????_??????_??????2() {
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
                .addressName("???????????? 132")
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
    @DisplayName("?????? ????????? ???????????? ?????? ???????????? ????????? ?????? ????????? ???????????? ?????? ????????? ????????????.")
    public void ??????_??????_??????3() {
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
                .addressName("???????????? 132")
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
    @DisplayName("????????? ???????????? ????????? ?????? ????????? ????????????.")
    public void ??????_??????_??????() {
        // given
        AddressDto requestDto = AddressDto.builder()
                .name("syt")
                .phone("01011112222")
                .addressName("???????????? 132")
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
    public void ??????_??????() {
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
    @DisplayName("?????? ????????? ???????????? ????????? ?????? ????????? ????????????.")
    public void ????????????_??????_??????() {
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
        given(addressRepository.findAddressByIdAndEmail(any(), any())).willReturn(Optional.of(newBaseAddress));
        given(addressRepository.findBaseAddress(any())).willReturn(Optional.of(preBaseAddress));

        // when
        memberService.changeBaseAddress("email", 1L);

        // then
        assertThat(preBaseAddress.getBaseAddress()).isFalse();
        assertThat(newBaseAddress.getBaseAddress()).isTrue();
    }

    @Test
    @DisplayName("????????? ????????????.")
    public void ??????_??????_??????1() {
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
        given(addressRepository.findAddressByIdAndEmail(any(), any())).willReturn(Optional.of(address2));

        // when
        AddressDto newAddress = memberService.updateAddress("email", 2L, updateAddress);

        // then
        assertThat(newAddress.getName()).isEqualTo("ccc");
    }

    @Test
    @DisplayName("?????? ????????? ?????? ????????? ?????? ????????? ????????????.")
    public void ??????_??????_??????2() {
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
        given(addressRepository.findAddressByIdAndEmail(any(), any())).willReturn(Optional.of(address2));
        given(addressRepository.findBaseAddress(any())).willReturn(Optional.of(address1));

        // when
        AddressDto newAddress = memberService.updateAddress("email", 2L, updateAddress);

        // then
        assertThat(newAddress.getName()).isEqualTo("ccc");
        assertThat(newAddress.getBaseAddress()).isTrue();
        assertThat(address1.getBaseAddress()).isFalse();
    }

    @Test
    @DisplayName("????????? ???????????? ????????? ?????? ????????? ?????? ??? ??????.")
    public void ??????_??????_??????1() {
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
        given(addressRepository.findAddressByIdAndEmail(any(), any())).willReturn(Optional.of(address1));
        given(addressRepository.findBaseAddress(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.updateAddress("email", 1L, updateAddress))
                .isInstanceOf(AddressNotFoundException.class);
    }

    @Test
    @DisplayName("?????? ????????? ???????????? BaseAddressUncheckedException ????????? ????????????.")
    public void ??????_??????_??????2() {
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
        given(addressRepository.findAddressByIdAndEmail(any(), any())).willReturn(Optional.of(address1));

        // when, then
        assertThatThrownBy(() -> memberService.updateAddress("email", 1L, updateAddress))
                .isInstanceOf(BaseAddressUncheckedException.class);
    }

    @Test
    @DisplayName("?????? ????????? ???????????? ????????? ????????? ??? ??????.")
    public void ??????_??????_??????() {
        // given
        Address address1 = Address.builder()
                .id(1L)
                .name("aaa")
                .phone("01011111111")
                .baseAddress(false)
                .build();

        // given
        given(addressRepository.findAddressByIdAndEmail(any(), any())).willReturn(Optional.of(address1));

        // when
        AddressDto addressDto = memberService.deleteAddress("aaa",1L);

        // then
        assertThat(addressDto.getName()).isEqualTo("aaa");
    }

    @Test
    @DisplayName("?????? ????????? ????????? ??? ??????.")
    public void ??????_??????_??????() {
        // given
        Address address1 = Address.builder()
                .id(1L)
                .name("aaa")
                .phone("01011111111")
                .baseAddress(true)
                .build();

        // given
        given(addressRepository.findAddressByIdAndEmail(any(), any())).willReturn(Optional.of(address1));

        // when, then
        assertThatThrownBy(() -> memberService.deleteAddress("aaa", 1L))
                .isInstanceOf(BaseAddressNotDeleteException.class);
    }

    @Test
    @DisplayName("????????? ???????????? ?????? Account ???????????? null?????? ?????????.")
    public void ??????_??????_??????1() {
        // given
        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        // when
        AccountDto responseDto = memberService.getAccount("email");

        // then
        assertThat(responseDto.getBankName()).isNull();
        assertThat(responseDto.getAccountNumber()).isNull();
        assertThat(responseDto.getAccountHolder()).isNull();
    }

    @Test
    @DisplayName("????????? ???????????? ????????? ????????? ????????? ????????????.")
    public void ??????_??????_??????2() {
        // given
        Account account = Account.builder()
                .bankName("??????")
                .accountNumber("????????????")
                .accountHolder("?????????")
                .build();

        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .account(account)
                .build();

        // mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        // when
        AccountDto responseDto = memberService.getAccount("email");

        // then
        assertThat(responseDto.getBankName()).isEqualTo(account.getBankName());
        assertThat(responseDto.getAccountNumber()).isEqualTo(account.getAccountNumber());
        assertThat(responseDto.getAccountHolder()).isEqualTo(account.getAccountHolder());
    }

    @Test
    @DisplayName("????????? ?????? ??? ????????? MemberNotFoundException ????????? ????????????.")
    public void ??????_??????_??????() {
        // given, mocking
        given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

        // when, then
        assertThatCode(() -> memberService.getAccount("email"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("????????? ?????? ???????????? ????????????.")
    public void ??????_??????_??????() {
        // given
        AccountDto requestDto = AccountDto.builder()
                .bankName("??????")
                .accountNumber("????????????")
                .accountHolder("?????????")
                .build();

        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .build();

        // mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.of(member));

        // when
        AccountDto responseDto = memberService.addAccount("email", requestDto);

        // then
        assertThat(responseDto.getBankName()).isEqualTo(requestDto.getBankName());
        assertThat(responseDto.getAccountNumber()).isEqualTo(requestDto.getAccountNumber());
        assertThat(responseDto.getAccountHolder()).isEqualTo(requestDto.getAccountHolder());
        assertThat(member.getAccount().getBankName()).isEqualTo(requestDto.getBankName());
        assertThat(member.getAccount().getAccountNumber()).isEqualTo(requestDto.getAccountNumber());
        assertThat(member.getAccount().getAccountHolder()).isEqualTo(requestDto.getAccountHolder());
    }

    @Test
    @DisplayName("????????? ???????????? ????????? ????????? ????????????.")
    public void ??????_??????_??????() {
        // given
        AccountDto requestDto = AccountDto.builder()
                .bankName("NEW ??????")
                .accountNumber("NEW ????????????")
                .accountHolder("NEW ?????????")
                .build();

        Account account = Account.builder()
                .bankName("??????")
                .accountNumber("????????????")
                .accountHolder("?????????")
                .build();

        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .account(account)
                .build();

        // mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.of(member));

        // when
        AccountDto responseDto = memberService.addAccount("email", requestDto);

        // then
        assertThat(responseDto.getBankName()).isEqualTo(requestDto.getBankName());
        assertThat(responseDto.getAccountNumber()).isEqualTo(requestDto.getAccountNumber());
        assertThat(responseDto.getAccountHolder()).isEqualTo(requestDto.getAccountHolder());
        assertThat(member.getAccount().getBankName()).isEqualTo(requestDto.getBankName());
        assertThat(member.getAccount().getAccountNumber()).isEqualTo(requestDto.getAccountNumber());
        assertThat(member.getAccount().getAccountHolder()).isEqualTo(requestDto.getAccountHolder());
    }

    @Test
    @DisplayName("???????????? ?????? ????????? ????????????.")
    public void ??????_??????_??????() {
        // given
        Account account = Account.builder()
                .bankName("??????")
                .accountNumber("????????????")
                .accountHolder("?????????")
                .build();

        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .account(account)
                .build();

        // mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.of(member));

        // when
        AccountDto responseDto = memberService.deleteAccount("email");

        // then
        assertThat(responseDto.getBankName()).isNull();
        assertThat(responseDto.getAccountNumber()).isNull();
        assertThat(responseDto.getAccountHolder()).isNull();
        assertThat(member.getAccount()).isNull();
    }

    @Test
    @DisplayName("????????? ???????????? ????????? ??? ??????.")
    public void ?????????_??????_??????() {
        // given
        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .point(100)
                .build();

        // mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.of(member));

        // when
        PointDto responseDto = memberService.getPoint("email");

        // then
        assertThat(responseDto.getPoint()).isEqualTo(member.getPoint());
    }

    @Test
    @DisplayName("????????? ????????? ????????????.")
    public void ?????????_??????_??????() {
        // given
        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .build();

        // mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.of(member));

        // when
        MemberDto responseDto = memberService.getProfile("email");

        // then
        assertThat(responseDto.getEmail()).isEqualTo("email");
        assertThat(responseDto.getShoeSize()).isEqualTo(0);
    }

    @Test
    @DisplayName("?????? ???????????? ????????? ???????????? ????????? MemberNotFoundException ????????? ????????????.")
    public void ?????????_??????_??????() {
        // given, mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.empty());

        // when, then
        assertThatCode(() -> memberService.getProfile("email"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("????????? ???????????? ?????? ??????????????? ???????????? ???????????? ????????? ????????????.")
    public void ????????????_??????_??????() {
        // given
        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .build();

        PasswordDto requestDto = PasswordDto.builder()
                .prePassword("PW")
                .newPassword("NEW PW")
                .build();

        // mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.of(member));
        given(passwordEncoder.matches(requestDto.getPrePassword(), member.getPassword())).willReturn(true);
        given(passwordEncoder.encode(any())).willReturn("encodedPW");

        // when
        memberService.changePassword("email", requestDto);

        // then
        assertThat(member.getPassword()).isNotEqualTo("PW");
        assertThat(member.getPassword()).isEqualTo("encodedPW");
    }

    @Test
    @DisplayName("???????????? ????????? ???????????? WrongPasswordException ????????? ????????????.")
    public void ????????????_??????_??????() {
        // given
        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .build();

        PasswordDto requestDto = PasswordDto.builder()
                .prePassword("PW")
                .newPassword("NEW PW")
                .build();

        // mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.of(member));
        given(passwordEncoder.matches(requestDto.getPrePassword(), member.getPassword())).willReturn(false);

        // when, then
        assertThatCode(() -> memberService.changePassword("email", requestDto))
                .isInstanceOf(WrongPasswordException.class);
    }

    @Test
    @DisplayName("????????? ???????????? ????????? ?????? ????????? ????????????.")
    public void ?????????_??????_??????_??????() {
        // given
        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .phone("01011111111")
                .build();

        // mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.of(member));

        // when
        memberService.changePhone("email", "01022222222");

        // then
        assertThat(member.getPhone()).isEqualTo("01022222222");
    }

    @Test
    @DisplayName("????????? ???????????? ?????? ????????? ????????? ????????????.")
    public void ??????_?????????_??????_??????() {
        // given
        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .shoeSize(255)
                .build();

        // mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.of(member));

        // when
        memberService.changeShoeSize("email", "270");

        // then
        assertThat(member.getShoeSize()).isEqualTo(270);
    }

    @Test
    @DisplayName("???????????? ?????? ???????????? ????????? ????????? NumberFormatException ????????? ????????????.")
    public void ??????_?????????_??????_??????() {
        // given
        Member member = Member.builder()
                .id(1L)
                .password("PW")
                .email("email")
                .shoeSize(255)
                .build();

        // mocking
        given(memberRepository.findByEmail("email")).willReturn(Optional.of(member));

        // when, then
        assertThatCode(() -> memberService.changeShoeSize("email", "asd"))
                .isInstanceOf(NumberFormatException.class);
    }
}