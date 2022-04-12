package com.study.shoestrade.repository;


import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.product.Product;

import javax.persistence.EntityManager;
import java.util.List;

import static com.study.shoestrade.domain.product.QProduct.product;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Product> findByNameContainsAndBrand_IdIn(String name, List<Long> brandNames) {
        return queryFactory
                .selectFrom(product)
                .where(nameEq(name),
                        brandNamesEq(brandNames))
                .fetch();
    }

    private Predicate brandNamesEq(List<Long> brandNames) {
        return brandNames.isEmpty() ? null : product.brand.id.in(brandNames);
    }

    private Predicate nameEq(String name) {
        return name.isEmpty() ? null : product.name.contains(name);
    }
}


