package com.study.shoestrade.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductSearchDto {

    private String name;

    private List<Long> brandIdList;
}
