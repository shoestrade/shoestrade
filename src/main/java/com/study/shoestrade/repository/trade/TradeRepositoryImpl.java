package com.study.shoestrade.repository.trade;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.member.QMember;
import com.study.shoestrade.domain.product.QProductImage;
import com.study.shoestrade.domain.trade.QTrade;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.response.QTradeLoadDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static com.querydsl.jpa.JPAExpressions.select;
import static com.study.shoestrade.domain.member.QMember.member;
import static com.study.shoestrade.domain.product.QProduct.product;
import static com.study.shoestrade.domain.product.QProductImage.productImage;
import static com.study.shoestrade.domain.product.QProductSize.productSize;
import static com.study.shoestrade.domain.trade.QTrade.trade;

public class TradeRepositoryImpl implements TradeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TradeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 입찰 검색
     *
     * @param email     사용자 이메일
     * @param tradeType 구매, 판매
     * @param pageable  페이지 정보
     * @return 검색된 입찰 내역
     */
    @Override
    public Page<TradeLoadDto> findTradeByEmailAndTradeType(String email, TradeType tradeType, Pageable pageable) {

        List<TradeLoadDto> content = queryFactory.select(new QTradeLoadDto(trade.id, product.korName, productSize.size, trade.price, productImage.name))
                .from(trade)
                .join(trade.productSize, productSize)
                .join(productSize.product, product)
                .join(productImage).on(product.eq(productImage.product))
                .join(memberType(tradeType), member)
                .where(member.email.eq(email), trade.tradeType.eq(tradeType), productImage.id.in(select(productImage.id.min()).from(productImage).groupBy(productImage.product)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    /**
     * 사용자 이메일과 입찰 id로 입찰 정보 가져옴
     *
     * @param email     사용자 이메일
     * @param tradeId   입찰 id
     * @param tradeType 입찰 타입
     * @return 검색 결과
     */
    @Override
    public List<Trade> findByIdAndEmail(String email, Long tradeId, TradeType tradeType) {
        return queryFactory.selectFrom(trade)
                .join(memberType(tradeType), member)
                .where(member.email.eq(email), trade.id.eq(tradeId))
                .fetch();
    }

    /**
     * 즉시 거래가
     *
     * @param productId  상품
     * @param tradeState 입찰 상태
     * @return 검색 결과
     */
    @Override
    public List<TradeLoadDto> findInstantTrade(Long productId, TradeState tradeState) {
        QTrade a = new QTrade("a");
        QTrade b = new QTrade("b");


        return queryFactory.select(new QTradeLoadDto(trade.id, productSize.size, trade.price))
                .from(trade)
                .join(trade.productSize, productSize)
                .where(productSize.product.id.eq(productId), trade.tradeState.eq(tradeState),
                        trade.id.in(
                                select(a.id.min())
                                        .from(a)
                                        .leftJoin(b)
                                        .on(a.productSize.eq(b.productSize), compareMinMax(tradeState, a, b))
                                        .where(b.productSize.isNull())
                                        .groupBy(a.productSize)
                        )
                )
                .orderBy(productSize.size.asc())
                .fetch();
    }

    private QMember memberType(TradeType tradeType) {
        return tradeType == TradeType.SELL ? trade.seller : trade.purchaser;
    }

    private BooleanExpression compareMinMax(TradeState tradeState, QTrade a, QTrade b) {
        return tradeState.equals(TradeState.SELL)
                ? a.price.lt(b.price).or(a.price.eq(b.price).and(b.lastModifiedDate.lt(a.lastModifiedDate)))
                : b.price.lt(a.price).or(a.price.eq(b.price).and(a.lastModifiedDate.lt(b.lastModifiedDate)));
    }
}
