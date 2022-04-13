package com.study.shoestrade.service.member;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;

    // 주소 등록
    public AddressDto addAddress(String email, AddressDto requestDto){
        log.info("MemberService -> addAddress 실행");

        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        List<Address> addressList = findMember.getAddressList();

        boolean isBaseAddress = requestDto.getBaseAddress();
        if(isBaseAddress && addressList.size() > 0){
            // 주소를 추가하는 동시에 기본 주소로 등록할 때
            preBaseAddressToFalse(email);
        }
        else if(addressList.size() == 0) {
            // 처음 주소를 등록할 때
            isBaseAddress = true;
        }

        Address address = requestDto.toEntity(findMember, isBaseAddress);
        //addressRepository.save(address);   -> cascade
        findMember.addAddress(address);

        return address.toAddressDto();
    }

    private void preBaseAddressToFalse(String email) {
        Address preBaseAddress = addressRepository.findBaseAddress(email)
                .orElseThrow(AddressNotFoundException::new);

        preBaseAddress.changeBaseAddress(false);
    }

    // 주소 목록 출력
    @Transactional(readOnly = true)
    public AddressListResponseDto getAddressList(String email, Pageable pageable){
        log.info("MemberService -> getAddressList 실행");

        return AddressListResponseDto.builder()
                .baseAddressDto(addressRepository.findBaseAddress(email).orElseThrow(AddressNotFoundException::new).toAddressDto())
                .addressDtoPage(addressRepository.findAddressList(email, pageable).map(Address::toAddressDto))
                .build();
    }

    // 기본 주소 변경
    public AddressDto changeBaseAddress(String email, Long id){
        log.info("MemberService -> changeBaseAddress 실행");

        Address newBaseAddress = addressRepository.findById(id)
                .orElseThrow(AddressNotFoundException::new);

        preBaseAddressToFalse(email);
        newBaseAddress.changeBaseAddress(true);

        return newBaseAddress.toAddressDto();
    }

    // 주소 수정
    public AddressDto updateAddress(String email, Long id, AddressDto requestDto){
        log.info("MemberService -> updateAddress 실행");

        Address findAddress = addressRepository.findById(id)
                .orElseThrow(AddressNotFoundException::new);

        if(requestDto.getBaseAddress()){
            preBaseAddressToFalse(email);
        }

        if(findAddress.getBaseAddress() && !requestDto.getBaseAddress()){
            // 기본 주소는 기본 주소를 해제할 수 없다.
            throw new BaseAddressUncheckedException();
        }

        findAddress.updateAddress(requestDto);
        return findAddress.toAddressDto();
    }

    // 주소 삭제
    public AddressDto deleteAddress(Long id){
        log.info("MemberService -> deleteAddress 실행");

        Address findAddress = addressRepository.findById(id)
                .orElseThrow(AddressNotFoundException::new);

        if(findAddress.getBaseAddress()){
            // 기본 주소는 삭제할 수 없다.
            throw new BaseAddressNotDeleteException();
        }

        AddressDto addressDto = findAddress.toAddressDto();
        addressRepository.delete(findAddress);
        return addressDto;
    }
}
