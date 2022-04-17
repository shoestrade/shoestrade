package com.study.shoestrade.repository.trade;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.response.TradeLoadDtoInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    /**
     * 판매 입찰 검색
     *
     * @param seller 판매자
     * @param tradeType 판매
     * @return 검색된 입찰 내역
     */
    @Query("select t.id as id, p.name as name, t.price as price from Trade t " +
            "join fetch ProductSize s on t.productSize = s " +
            "join fetch Product p on s.product = p " +
            "where t.seller = :seller and t.tradeType = :tradeType")
    Page<TradeLoadDtoInterface> findTradeByEmailAndTradeType(Member seller, TradeType tradeType, Pageable pageable);
}
