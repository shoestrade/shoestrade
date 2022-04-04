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
     * @param name
     * @return
     */
    List<Brand> findByNameContains(String name);

    /**
     * 이름으로 검색
     * @param name
     * @return
     */
    Optional<Brand> findByName(String name);
}
