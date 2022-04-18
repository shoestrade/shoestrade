package com.study.shoestrade.repository;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.product.ProductSize;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.repository.brand.BrandRepository;
import com.study.shoestrade.repository.product.ProductRepository;
import com.study.shoestrade.repository.product.ProductSizeRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TradeRepositoryTest {

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductSizeRepository productSizeRepository;

    @Autowired
    BrandRepository brandRepository;

    Member member = Member.builder()
            .email("0tkk@naver.com")
            .password("1234")
            .shoeSize(255)
            .build();

    @Test
    @DisplayName("판매_입찰_등록_테스트")
    public void 판매_입찰_등록() {
        // given
        Trade trade = Trade.builder()
                .price(10000)
                .productSize(ProductSize.builder().id(1L).size(255).build())
                .seller(member)
                .tradeState(TradeState.SELL)
                .tradeType(TradeType.SELL)
                .build();

        // when
        Trade findTrade = tradeRepository.save(trade);

        // then
        assertThat(findTrade).isEqualTo(trade);
    }
}