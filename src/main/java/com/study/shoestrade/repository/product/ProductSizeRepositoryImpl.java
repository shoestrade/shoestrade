package com.study.shoestrade.repository.product;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.interest.QInterestProduct;
import com.study.shoestrade.domain.member.QMember;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.domain.product.QProduct;
import com.study.shoestrade.domain.product.QProductSize;

import javax.persistence.EntityManager;
import java.util.List;

import static com.study.shoestrade.domain.interest.QInterestProduct.interestProduct;
import static com.study.shoestrade.domain.member.QMember.member;
import static com.study.shoestrade.domain.product.QProduct.*;
import static com.study.shoestrade.domain.product.QProductSize.productSize;

public class ProductSizeRepositoryImpl implements ProductSizeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ProductSizeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

}
