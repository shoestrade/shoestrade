package com.study.shoestrade.repository.brand;

import com.study.shoestrade.domain.product.Brand;

import java.util.List;

public interface BrandRepositoryCustom {

    /**
     * 검색한 문자가 포함된 Brand 반환
     * @param name 검색어
     * @return 검색 결과
     */
    List<Brand> findByNameContains(String name);
}
