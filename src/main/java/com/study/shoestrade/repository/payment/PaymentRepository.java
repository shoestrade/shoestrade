package com.study.shoestrade.repository.payment;

import com.study.shoestrade.domain.payment.Payment;
import com.study.shoestrade.domain.trade.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderIdAndTrade(String orderId, Trade trade);
}
