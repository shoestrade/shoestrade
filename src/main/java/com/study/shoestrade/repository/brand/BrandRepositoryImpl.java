package com.study.shoestrade.repository.brand;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.product.Brand;

import javax.persistence.EntityManager;
import java.util.List;

import static com.study.shoestrade.domain.product.QBrand.brand;

public class BrandRepositoryImpl implements BrandRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BrandRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Brand> findByNameContains(String name) {
        return queryFactory
                .selectFrom(brand)
                .where(brandContains(name))
                .fetch();
    }

    private BooleanExpression brandContains(String name) {
        return name.isEmpty() ? null : brand.name.contains(name);
    }
}
