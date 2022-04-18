package com.study.shoestrade.repository.product;

import com.study.shoestrade.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {

    Page<Product> findProduct(String name, List<Long> brandNames, Pageable pageable);
}
