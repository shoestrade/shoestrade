package com.study.shoestrade.repository.trade;

import com.study.shoestrade.domain.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {

}
