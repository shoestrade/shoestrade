package com.study.shoestrade.service;

import com.study.shoestrade.dto.ProductDto;

import java.util.List;

public interface ProductService {

    /**
     * 상품 등록
     *
     * @param productDto 등록할 상품 정보
     * @return 등록한 상품 id
     */
    ProductDto saveProduct(ProductDto productDto);


    /**
     * 상품 전체 검색
     *
     * @return 검색 결과
     */
    List<ProductDto> findProductAll();


    /**
     * 상품 이름으로 검색
     *
     * @return 검색 결과
     */
    List<ProductDto> findProductByName(String name);


    /**
     * 상품 정보 변경
     * @param productDto 변경할 정보
     */
    void updateProduct(ProductDto productDto);

}
