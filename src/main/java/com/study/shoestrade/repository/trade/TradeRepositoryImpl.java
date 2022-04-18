package com.study.shoestrade.repository.trade;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.member.QMember;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.response.QTradeLoadDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.study.shoestrade.domain.member.QMember.member;
import static com.study.shoestrade.domain.product.QProduct.product;
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
     * @return 검색된 입찰 내역
     */
    @Override
    public Page<TradeLoadDto> findTradeByEmailAndTradeType(String email, TradeType tradeType, Pageable pageable) {

        List<TradeLoadDto> content = queryFactory.select(new QTradeLoadDto(trade.id, product.korName, trade.price))
                .from(trade)
                .leftJoin(trade.productSize, productSize)
                .leftJoin(productSize.product, product)
                .leftJoin(memberType(tradeType), member)
                .where(member.email.eq(email), trade.tradeType.eq(tradeType))
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
                .leftJoin(memberType(tradeType), member)
                .where(member.email.eq(email), trade.id.eq(tradeId))
                .fetch();
    }

    private QMember memberType(TradeType tradeType) {
        return tradeType == TradeType.SELL ? trade.seller : trade.purchaser;
    }
}
