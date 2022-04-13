package com.study.shoestrade.repository.product;

import com.study.shoestrade.domain.product.Product;

import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> findProduct(String name, List<Long> brandNames);
}
