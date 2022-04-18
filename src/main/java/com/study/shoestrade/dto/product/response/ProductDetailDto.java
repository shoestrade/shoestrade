package com.study.shoestrade.dto.product.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {

    private Long id;
    private String name;
    private String code;
    private String color;
    private int releasePrice;
    private int interest;
    private String brandName;

    private int lastedPrice;
    private int ImSalesPrice;
    private int ImPurchasePrice;

    private List<String> imageList = new ArrayList<>();



}
