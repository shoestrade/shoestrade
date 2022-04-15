package com.study.shoestrade.repository.brand;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.product.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.study.shoestrade.domain.product.QBrand.brand;

public class BrandRepositoryImpl implements BrandRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BrandRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Brand> findByNameContains(String name, Pageable pageable) {
        List<Brand> content = queryFactory
                .selectFrom(brand)
                .where(brandContains(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    private BooleanExpression brandContains(String name) {
        return name == null || name.isEmpty() ? null : brand.name.contains(name);
    }
}
