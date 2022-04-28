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
import com.study.shoestrade.dto.trade.response.QTradeBreakdownCountDto;
import com.study.shoestrade.dto.trade.response.QTradeLoadDto;
import com.study.shoestrade.dto.trade.response.TradeBreakdownCountDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import com.study.shoestrade.exception.trade.WrongStateException;
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

    // 거래 내역 조회
    @Override
    public Page<TradeLoadDto> findBreakdown(String email, TradeType tradeType, String state, Pageable pageable) {
        List<TradeLoadDto> content = queryFactory.select(
                new QTradeLoadDto(
                        trade.id,
                        product.korName,
                        productSize.size,
                        trade.price,
                        trade.tradeCompletionDate,
                        trade.tradeState,
                        productImage.name)
                )
                .from(trade)
                .join(trade.productSize, productSize)
                .join(productSize.product, product)
                .join(productImage).on(product.eq(productImage.product))
                .join(memberType(tradeType), member)
                .where(
                        trade.tradeState.in(getStateList(state, tradeType)),
                        member.email.eq(email), trade.tradeType.eq(tradeType),
                        productImage.id.in(select(productImage.id.min()).from(productImage).groupBy(productImage.product))
                )
                .orderBy(trade.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(trade.count())
                .from(trade)
                .join(memberType(tradeType), member)
                .where(trade.tradeState.in(getStateList(state, tradeType)),
                        member.email.eq(email), trade.tradeType.eq(tradeType))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
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

    // 거래 내역 수 조회
    @Override
    public TradeBreakdownCountDto findBreakdownCount(String email, TradeType tradeType) {
        return queryFactory.select(new QTradeBreakdownCountDto(
                        select(trade.count())
                                .from(trade)
                                .join(memberType(tradeType), member)
                                .where(trade.tradeType.eq(tradeType), trade.tradeState.eq(getTradeState(tradeType)), member.email.eq(email)),
                        select(trade.count())
                                .from(trade)
                                .join(memberType(tradeType), member)
                                .where(trade.tradeType.eq(tradeType), trade.tradeState.in(progressTrade()), member.email.eq(email)),
                        select(trade.count())
                                .from(trade)
                                .join(memberType(tradeType), member)
                                .where(trade.tradeType.eq(tradeType), trade.tradeState.eq(TradeState.DONE), member.email.eq(email))
                        )
                )
                .from(trade)
                .distinct()
                .fetchOne();
    }

    private static List<TradeState> progressTrade(){
        return List.of(TradeState.CENTER_DELIVERY, TradeState.INSPECT, TradeState.FAKE, TradeState.REAL, TradeState.HOME_DELIVERY);
    }

    // 내역에서 입찰, 진행 중, 종료인지
    private List<TradeState> getStateList(String state, TradeType tradeType){
        if(state.equals("bid")){
            return List.of(getTradeState(tradeType));
        }
        else if(state.equals("progress")){
            return progressTrade();
        }
        else{
            return List.of(TradeState.DONE);
        }
    }

    private TradeState getTradeState(TradeType tradeType){
        return tradeType == TradeType.PURCHASE ? TradeState.PURCHASE : TradeState.SELL;
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
