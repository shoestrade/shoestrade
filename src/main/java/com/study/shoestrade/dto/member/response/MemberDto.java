package com.study.shoestrade.dto.member.response;


import com.study.shoestrade.domain.member.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;

    private String email;

//    @ApiModelProperty(value = "비밀번호")
//    private String password;

    private String name;

//    @ApiModelProperty(value = "휴대폰 번호")
//    private String phone;

//    @ApiModelProperty(value = "주소")
//    private Address address;

    private int shoeSize;

//    @ApiModelProperty(value = "계좌")
//    private Account account;

    public static MemberDto create(Member member){
        return new MemberDto(member.getId(), member.getEmail(), member.getName(),
                member.getShoeSize());
    }

}
