package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Address;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.address.AddressDto;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.repository.AddressRepository;
import com.study.shoestrade.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
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
        assertThat(responseDto.isBaseAddress()).isTrue();
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
        assertThat(responseDto.isBaseAddress()).isFalse();
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
        addresses.add(Address.builder()
                .name("aaa")
                .phone("01011111111")
                .baseAddress(false)
                .build());

        addresses.add(Address.builder()
                .name("bbb")
                .phone("01022222222")
                .baseAddress(false)
                .build());

        // mocking
        given(addressRepository.findAddressList(any())).willReturn(addresses);

        // when
        List<AddressDto> addressList = memberService.getAddressList(any());

        // then
        assertThat(addressList.size()).isEqualTo(2);
    }
}