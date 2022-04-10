package com.study.shoestrade.repository;

import com.study.shoestrade.domain.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    /**
     * 검색한 문자가 포함된 Brand 반환
     * @param name 검색어
     * @return 검색 결과
     */
    List<Brand> findByNameContains(String name);

    /**
     * 동일한 이름 가진 브랜드 검색
     * @param name 검색어
     * @return 검색 결과
     */
    Optional<Brand> findByName(String name);
}
