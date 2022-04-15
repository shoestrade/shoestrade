package com.study.shoestrade.repository.product;


import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.study.shoestrade.domain.product.QProduct.product;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Product> findProduct(String name, List<Long> brandNames, Pageable pageable) {
        List<Product> content = queryFactory
                .selectFrom(product)
                .where(nameEq(name),
                        brandNamesEq(brandNames))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    private BooleanExpression brandNamesEq(List<Long> brandNames) {
        return brandNames.isEmpty() ? null : product.brand.id.in(brandNames);
    }

    private BooleanExpression nameEq(String name) {
        return name.isEmpty() ? null : product.name.contains(name);
    }
}


