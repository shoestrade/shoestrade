package com.study.shoestrade.repository.brand;

import com.study.shoestrade.domain.product.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandRepositoryCustom {

    /**
     * 검색한 문자가 포함된 Brand 반환
     * @param name 검색어
     * @param pageable 페이지 정보
     * @return 검색 결과
     */
    Page<Brand> findByNameContains(String name, Pageable pageable);
}
