package com.study.shoestrade.domain.trade;

public enum TradeState {
    SELL, PURCHASE,
    READY,  // 결제 대기 중
    COMPLETE,  // 결제 완료
    CENTER_DELIVERY, INSPECT, FAKE, REAL, HOME_DELIVERY, DONE,
    FAIL  // 판매 체결 후 청구 기간 동안 돈을 지불하지 않음
}
