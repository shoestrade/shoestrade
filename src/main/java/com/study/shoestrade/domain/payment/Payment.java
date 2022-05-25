package com.study.shoestrade.domain.payment;

import com.study.shoestrade.domain.BaseEntity;
import com.study.shoestrade.domain.trade.Trade;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Payment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "trade_id")
    private Trade trade;

    private String impId;  // 아임포트 생성 주문 번호
    private String orderId;  // 주문 번호

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;  // 결제 수단

    private String name;  // 상품 이름

    private int price;  // 결제 금액
    private int point;  // 포인트 사용 금액

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.READY;  // 상태

    private LocalDateTime paidAt;  // 결제 완료 일시

    public void successPayment(String impId, PaymentMethod method, PaymentStatus status, LocalDateTime paidAt){
        this.impId = impId;
        this.method = method;
        this.status = status;
        this.paidAt = paidAt;
    }
}
