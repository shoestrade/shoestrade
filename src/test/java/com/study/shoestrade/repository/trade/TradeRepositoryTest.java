package com.study.shoestrade.repository.trade;

import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TradeRepositoryTest {

    @Autowired
    TradeRepository tradeRepository;

    @Test
    @DisplayName("판매_입찰_검색_테스트")
    public void 판매_입찰_검색() {
        // given
        Trade.builder()
                .price(10000)
                .tradeType(TradeType.SELL)
                .tradeState(TradeState.SELL)

                .build();


        // when


        // then
    }

}