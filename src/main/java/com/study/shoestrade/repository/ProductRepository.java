package com.study.shoestrade.repository;

import com.study.shoestrade.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    List<Product> findByNameContains(String name);

    List<Product> findByNameContainsAndBrand_IdIn(String name, List<Long> brandNames);

    List<Product> findByBrandId(Long id);
}
