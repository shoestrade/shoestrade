package com.study.shoestrade.repository.product;

import com.study.shoestrade.dto.product.response.ProductDetailDto;
import com.study.shoestrade.dto.product.response.ProductLoadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {

    /**
     * 상품 검색
     *
     * @param name       검색어
     * @param brandNames 선택할 브랜드
     * @param pageable   페이지 정보
     * @return 검색 결과
     */
    Page<ProductLoadDto> findProduct(String name, List<Long> brandNames, Pageable pageable);

    /**
     * 상품 상세 검색
     *
     * @param productId 검색할 상품 id
     * @return 검색 결과
     */
    Optional<ProductDetailDto> findProductDetail(Long productId);
}
