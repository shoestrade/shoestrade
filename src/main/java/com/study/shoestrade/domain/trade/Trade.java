package com.study.shoestrade.domain.trade;

import com.study.shoestrade.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trade {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "trade_id")
    private Long id;

    private int price;

    @Enumerated
    private TradeType tradeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaser_id")
    private Member purchaser;

    @Enumerated
    private TradeState tradeState;

    @Builder
    public Trade(Long id, int price, TradeType tradeType, Member seller, Member purchaser, TradeState tradeState) {
        this.id = id;
        this.price = price;
        this.tradeType = tradeType;
        this.seller = seller;
        this.purchaser = purchaser;
        this.tradeState = tradeState;
    }
}
