package com.study.shoestrade.service.product;

import com.study.shoestrade.dto.product.ProductImageDto;
import com.study.shoestrade.dto.product.request.ProductSaveDto;
import com.study.shoestrade.dto.product.ProductImageAddDto;
import com.study.shoestrade.dto.product.request.ProductSearchDto;
import com.study.shoestrade.dto.product.response.ProductDetailDto;
import com.study.shoestrade.dto.product.response.ProductLoadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {

    /**
     * 상품 등록
     *
     * @param productDto 등록할 상품 정보
     */
    void saveProduct(ProductSaveDto productDto);

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
     * @param pageable         페이지 정보
     * @return 검색 결과
     */
    Page<ProductLoadDto> findProductByNameInBrand(ProductSearchDto productSearchDto, Pageable pageable);

    /**
     * 상품 정보 변경
     *
     * @param productDto 변경할 정보
     */
    void updateProduct(Long id, ProductSaveDto productDto);

    /**
     * 상품 이미지 검색
     *
     * @param productId 검색할 상품 id
     */
    List<ProductImageDto> findProductImageByProductId(Long productId);

    /**
     * 상품 이미지 등록
     *
     * @param productId 등록할 상품 id
     * @param productImageAddDto 이미지 정보
     */
    void addProductImage(Long productId, ProductImageAddDto productImageAddDto);

    /**
     * 이미지 삭제
     *
     * @param productImageId 삭제할 이미지 id
     */
    void deleteProductImage(Long productImageId);


    /**
     * 상품 상세 검색
     *
     * @param productId 검색할 상품 id
     * @return 검색 결과
     */
    ProductDetailDto findProductDetailById(Long productId);
}
