package com.study.shoestrade.domain.trade;

import com.study.shoestrade.domain.BaseEntity;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.product.ProductSize;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Trade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    private int price;
    private LocalDateTime tradeCompletionDate;
    private LocalDateTime claimDueDate;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaser_id")
    private Member purchaser;

    @Enumerated(EnumType.STRING)
    private TradeState tradeState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productSize_id")
    private ProductSize productSize;

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeState(TradeState state){
        this.tradeState = state;
    }

    public void changeSeller(Member member){
        this.seller = member;
    }

    public void changePurchaser(Member member){
        this.purchaser = member;
    }

    public void changeClaimDueDate(LocalDateTime deadline){
        this.claimDueDate = deadline;
    }

    public void finishTrade(LocalDateTime tradeCompletionDate){
        this.tradeCompletionDate = tradeCompletionDate;
    }
}
