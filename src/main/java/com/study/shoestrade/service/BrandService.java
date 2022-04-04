package com.study.shoestrade.service;


import com.study.shoestrade.dto.BrandDto;

import java.util.List;

public interface BrandService {

    long saveBrand(String name);

    void updateBrand(BrandDto brandDto);

    void deleteByBrandId(Long id);

    List<BrandDto> findBrandAll();

    List<BrandDto> findByBrandName(String name);
}

