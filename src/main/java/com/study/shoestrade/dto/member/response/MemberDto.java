package com.study.shoestrade.dto.member.response;


import com.study.shoestrade.domain.member.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private String email;
    private String name;
    private String phone;
    private int shoeSize;

    public static MemberDto create(Member member){
        return new MemberDto(member.getEmail(), member.getName(), member.getPhone(), member.getShoeSize());
    }

}
