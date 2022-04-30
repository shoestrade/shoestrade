package com.study.shoestrade.dto.member.response;


import com.study.shoestrade.domain.member.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @ApiModelProperty(example = "cjswltjr159@naver.com", value = "email")
    private String email;

    @ApiModelProperty(example = "천지석", value = "이름")
    private String name;

    @ApiModelProperty(example = "010-1224-1231", value = "휴대폰 번호")
    private String phone;
    @ApiModelProperty(example = "255", value = "신발 사이즈")
    private int shoeSize;

    public static MemberDto create(Member member){
        return new MemberDto(member.getEmail(), member.getName(), member.getPhone(), member.getShoeSize());
    }

}
