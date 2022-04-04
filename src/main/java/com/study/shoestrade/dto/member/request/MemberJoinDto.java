package com.study.shoestrade.dto.member.request;


import com.study.shoestrade.domain.member.*;
import lombok.Getter;

@Getter
public class MemberJoinDto {

    private String email;

    private String password;

    private String name;

    private String phone;

//    @ApiModelProperty(value = "주소")
//    private Address address;

    private int shoeSize;

//    @ApiModelProperty(value = "계좌")
//    private Account account;

    public Member toEntity(String password){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
//                .address(address)
                .point(0)
                .grade(Grade.BRONZE)
                .shoeSize(shoeSize)
                .role(Role.MEMBER)
//                .account(account)
                .build();
    }

}
