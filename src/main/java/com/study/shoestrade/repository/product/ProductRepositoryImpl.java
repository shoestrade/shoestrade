package com.study.shoestrade.repository.product;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.product.*;
import com.study.shoestrade.domain.trade.QTrade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.dto.product.response.ProductDetailDto;
import com.study.shoestrade.dto.product.response.QProductDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.*;
import static com.study.shoestrade.domain.product.QProduct.product;
import static com.study.shoestrade.domain.product.QProductSize.productSize;
import static com.study.shoestrade.domain.trade.QTrade.trade;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 상품 검색
     *
     * @param name       검색어
     * @param brandNames 선택할 브랜드
     * @param pageable   페이지 정보
     * @return 검색 결과
     */
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

    /**
     *
     * @param productId 검색할 상품 id
     * @return 검색 결과
     */
    @Override
    public Optional<ProductDetailDto> findProductDetail(Long productId) {
        List<ProductDetailDto> content = queryFactory.select(
                        new QProductDetailDto(
                                product,
                                select(trade.price)
                                        .from(trade)
                                        .leftJoin(trade.productSize, productSize)
                                        .where(trade.tradeState.eq(TradeState.DONE), productSize.product.id.eq(productId))
                                        .orderBy(trade.lastModifiedDate.desc()),
                                select(trade.price.min())
                                        .from(trade)
                                        .leftJoin(trade.productSize, productSize)
                                        .where(trade.tradeState.eq(TradeState.SELL), productSize.product.id.eq(productId)),
                                select(trade.price.max())
                                        .from(trade)
                                        .leftJoin(trade.productSize, productSize)
                                        .where(trade.tradeState.eq(TradeState.PURCHASE), productSize.product.id.eq(productId)))
                )
                .from(product)
                .where(product.id.eq(productId))
                .fetch();

        return Optional.ofNullable(content.size() != 0 ? content.get(0) : null);
    }

    private BooleanExpression brandNamesEq(List<Long> brandNames) {
        return brandNames.isEmpty() ? null : product.brand.id.in(brandNames);
    }

    private BooleanExpression nameEq(String name) {
        return name.isEmpty() ? null : product.korName.contains(name).or(product.engName.contains(name));
    }

}


