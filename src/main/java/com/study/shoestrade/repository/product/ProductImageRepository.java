package com.study.shoestrade.repository.product;

import com.study.shoestrade.domain.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    /**
     * 상품 내 상품 이미지 이름 검색
     *
     * @param productId 상품 id
     * @param names     상품 이미지 이름
     * @return 검색 결과
     */
    List<ProductImage> findByProductIdAndNameIn(Long productId, List<String> names);


    /**
     * 상품 이미지 검색
     *
     * @param productId 상품 id
     * @return 검색 결과
     */
    List<ProductImage> findByProductId(Long productId);
}
