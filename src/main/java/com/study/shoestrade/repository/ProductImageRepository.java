package com.study.shoestrade.repository;

import com.study.shoestrade.domain.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductIdAndNameIn(Long productId, List<String> names);
}
