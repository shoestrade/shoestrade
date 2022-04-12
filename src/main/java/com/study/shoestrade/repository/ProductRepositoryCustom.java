package com.study.shoestrade.repository;

import com.study.shoestrade.domain.product.Product;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> findByNameContainsAndBrand_IdIn(String name, List<Long> brandNames);
}
