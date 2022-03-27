package com.study.shoestrade;

import com.study.shoestrade.domain.product.Product;
import com.study.shoestrade.domain.trade.Trade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
@Rollback(value = false)
@SpringBootTest
public class DataTest {

    @Autowired
    EntityManager em;

    @Test
    void test(){
        Product product = Product.builder()
                .color("asd")
                .build();

        Trade trade = Trade.builder()
                .price(123)
                .build();

        em.persist(product);
        em.persist(trade);
        em.flush();
        em.clear();
    }
}
