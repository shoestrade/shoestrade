package com.study.shoestrade.dto.payment.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PaymentVerifyRequestDto {

    @ApiModelProperty(example = "ST20220525_123132", value = "주문 번호")
    private String orderId;

    @ApiModelProperty(example = "imp_123123", value = "아임포트에서 생성된 주문 번호")
    private String impId;

    @ApiModelProperty(example = "3", value = "구매할 trade id")
    private Long tradeId;
}