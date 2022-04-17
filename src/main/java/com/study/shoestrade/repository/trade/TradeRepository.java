package com.study.shoestrade.repository.trade;

import com.study.shoestrade.domain.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long>, TradeRepositoryCustom {
}
