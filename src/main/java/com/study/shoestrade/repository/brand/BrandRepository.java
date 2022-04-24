package com.study.shoestrade.repository.brand;

import com.study.shoestrade.domain.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long>, BrandRepositoryCustom {

    /**
     * 동일한 한국 이름 가진 브랜드 검색
     *
     * @param korName 검색어
     * @return 검색 결과
     */
    Optional<Brand> findByKorName(String korName);

    /**
     * 동일한 영어 이름 가진 브랜드 검색
     *
     * @param engName 검색어
     * @return 검색 결과
     */
    Optional<Brand> findByEngName(String engName);
}
