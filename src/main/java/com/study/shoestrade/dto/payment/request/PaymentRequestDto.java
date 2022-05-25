package com.study.shoestrade.dto.payment.request;

import com.study.shoestrade.domain.payment.PaymentMethod;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PaymentRequestDto {

    @ApiModelProperty(example = "CARD", value = "결제 수단")
    private PaymentMethod method;

    @ApiModelProperty(example = "나이키 에어맥스 97", value = "상품 이름")
    private String name;

    @ApiModelProperty(example = "100000", value = "실제 결제할 가격")
    private int price;

    @ApiModelProperty(example = "100", value = "사용할 포인트")
    private int point;

    @ApiModelProperty(example = "3", value = "구매할 trade id")
    private Long tradeId;
}
