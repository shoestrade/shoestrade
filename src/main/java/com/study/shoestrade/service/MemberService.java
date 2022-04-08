package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Address;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.address.AddressDto;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.repository.AddressRepository;
import com.study.shoestrade.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    // 주소 추가
    public AddressDto addAddress(String email, AddressDto requestDto){
        log.info("MemberService -> addAddress 실행");

        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        List<Address> addressList = findMember.getAddressList();

        boolean isBaseAddress = requestDto.isBaseAddress();
        if(addressList.size() == 0) {
            isBaseAddress = true;
        }

        Address address = requestDto.toEntity(findMember, isBaseAddress);
        //addressRepository.save(address);   -> cascade
        findMember.addAddress(address);

        return address.toAddressDto();
    }

    // 주소 목록 출력
    @Transactional(readOnly = true)
    public List<AddressDto> getAddressList(String email){
        log.info("MemberService -> getAddressList 실행");

        List<Address> addressList = addressRepository.findAddressByMemberEmailOrderByBaseAddress(email);
        return addressList.stream()
                .map(Address::toAddressDto)
                .collect(Collectors.toList());
    }
}
