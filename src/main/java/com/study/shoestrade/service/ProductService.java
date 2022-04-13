package com.study.shoestrade.service;

import com.study.shoestrade.dto.ProductDto;
import com.study.shoestrade.dto.ProductImageAddDto;
import com.study.shoestrade.dto.ProductSearchDto;

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
     * 상품 삭제
     *
     * @param productId 삭제할 상품 id
     */
    void deleteProduct(Long productId);

    /**
     * 상품 검색
     *
     * @param productSearchDto 검색어, 브랜드 이름 리스트
     * @return 검색 결과
     */
    List<ProductDto> findProductByNameInBrand(ProductSearchDto productSearchDto);

    /**
     * 상품 정보 변경
     *
     * @param productDto 변경할 정보
     */
    void updateProduct(ProductDto productDto);

    /**
     * 상품 이미지 등록
     *
     * @param productImageAddDto 등록할 상품 id, 이미지 정보
     */
    void addProductImage(ProductImageAddDto productImageAddDto);

    /**
     * 이미지 삭제
     * @param productImageId 삭제할 이미지 id
     */
    void deleteProductImage(Long productImageId);
}
