package com.study.shoestrade.service.product;

import com.study.shoestrade.dto.product.ProductDto;
import com.study.shoestrade.dto.product.ProductImageAddDto;
import com.study.shoestrade.dto.product.request.ProductSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


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
    Page<ProductDto> findProductByNameInBrand(ProductSearchDto productSearchDto, Pageable pageable);

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
