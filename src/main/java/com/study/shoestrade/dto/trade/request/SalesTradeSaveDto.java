package com.study.shoestrade.dto.trade.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesTradeSaveDto {

    private int price;

    private Long productSizeId;
}
