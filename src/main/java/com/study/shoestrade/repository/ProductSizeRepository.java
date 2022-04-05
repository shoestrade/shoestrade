package com.study.shoestrade.repository;

import com.study.shoestrade.domain.product.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
}
