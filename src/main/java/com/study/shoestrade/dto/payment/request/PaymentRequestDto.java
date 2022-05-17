package com.study.shoestrade.dto.payment.request;

import com.study.shoestrade.domain.payment.PaymentMethod;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PaymentRequestDto {
    private PaymentMethod method;
    private String name;
    private int price;
    private int point;
    private Long tradeId;
}
