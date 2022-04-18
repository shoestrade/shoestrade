package com.study.shoestrade.repository.product;

import com.study.shoestrade.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Optional<Product> findByKorName(String korName);

    Optional<Product> findByEngName(String engName);
}
