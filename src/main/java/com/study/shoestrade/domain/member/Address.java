package com.study.shoestrade.domain.member;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {

    @NotNull
    private String addressName; // 주소

    @NotNull
    private String detail; // 상세 주소

    @NotNull
    private String zipcode; // 우편번호

}
