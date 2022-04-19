package com.study.shoestrade.repository.product;

import com.study.shoestrade.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    /**
     * 동일한 한국 이름 가진 상품 검색
     *
     * @param korName 검색어
     * @return 검색 결과
     */
    Optional<Product> findByKorName(String korName);

    /**
     * 동일한 영어 이름 가진 상품 검색
     *
     * @param engName 검색어
     * @return 검색 결과
     */
    Optional<Product> findByEngName(String engName);
}
