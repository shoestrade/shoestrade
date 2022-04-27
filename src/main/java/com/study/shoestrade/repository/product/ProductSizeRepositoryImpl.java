package com.study.shoestrade.repository.product;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class ProductSizeRepositoryImpl implements ProductSizeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ProductSizeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

}
