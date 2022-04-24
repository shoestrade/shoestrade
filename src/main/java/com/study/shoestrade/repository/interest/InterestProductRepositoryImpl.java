package com.study.shoestrade.repository.interest;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.interest.InterestProduct;
import com.study.shoestrade.domain.product.QProduct;
import com.study.shoestrade.domain.product.QProductImage;
import com.study.shoestrade.domain.trade.QTrade;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.interest.response.MyInterest;
import com.study.shoestrade.dto.interest.response.QMyInterest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.study.shoestrade.domain.interest.QInterestProduct.*;
import static com.study.shoestrade.domain.member.QMember.*;
import static com.study.shoestrade.domain.product.QProduct.product;
import static com.study.shoestrade.domain.product.QProductImage.*;
import static com.study.shoestrade.domain.product.QProductSize.*;
import static com.study.shoestrade.domain.trade.QTrade.trade;

public class InterestProductRepositoryImpl implements InterestProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public InterestProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<InterestProduct> findPreInterests(String email, Long productId) {
        return queryFactory
                .selectFrom(interestProduct)
                .leftJoin(interestProduct.member, member)
                .leftJoin(interestProduct.productSize, productSize)
                .where(member.email.eq(email), productSize.product.id.eq(productId))
                .fetch();
    }

    @Override
    public Page<MyInterest> findMyInterests(String email, Pageable pageable) {
        List<MyInterest> content = queryFactory
                .select(new QMyInterest(interestProduct.id, product.id, productSize.id, product.brand.engName,
                        product.korName, productSize.size, trade.price.min(), productImage.name))
                .from(interestProduct)
                .join(interestProduct.member, member)
                .join(interestProduct.productSize, productSize)
                .join(productSize.product, product)
                .join(productImage).on(productImage.product.eq(product))
                .leftJoin(trade).on(trade.productSize.eq(productSize))
                .where(member.email.eq(email),
                        productImage.id.in(JPAExpressions.select(productImage.id.min()).from(productImage).groupBy(productImage.product.id)),
                        trade.tradeType.eq(TradeType.SELL).or(trade.isNull())
                        )
                .groupBy(interestProduct.id, productImage.name)
                .orderBy(interestProduct.lastModifiedDate.asc(), interestProduct.productSize.id.asc())
                .offset(pageable.getOffset())
                .limit(10)
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    @Override
    public List<MyInterest> findMemberInterests(Long id) {
        return queryFactory
                .select(new QMyInterest(interestProduct.id, product.id, productSize.id, product.brand.engName,
                        product.korName, productSize.size, trade.price.min(), productImage.name))
                .from(interestProduct)
                .join(interestProduct.member, member)
                .join(interestProduct.productSize, productSize)
                .join(productSize.product, product)
                .join(productImage).on(productImage.product.eq(product))
                .leftJoin(trade).on(trade.productSize.eq(productSize))
                .where(member.id.eq(id),
                        productImage.id.in(JPAExpressions.select(productImage.id.min()).from(productImage).groupBy(productImage.product.id)),
                        trade.tradeType.eq(TradeType.SELL).or(trade.isNull())
                )
                .groupBy(interestProduct.id, productImage.name)
                .orderBy(interestProduct.lastModifiedDate.asc(), interestProduct.productSize.id.asc())
                .fetch();
    }


}
