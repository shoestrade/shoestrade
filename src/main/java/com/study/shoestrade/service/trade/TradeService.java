package com.study.shoestrade.service.trade;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.dto.trade.request.SalesTradeSaveDto;

public interface TradeService {

    /**
     * 판매 입찰 등록
     * @param member 판매자 이메일
     * @param salesTradeSaveDto 판매 정보
     */
    void salesTradeSave(Member member, SalesTradeSaveDto salesTradeSaveDto);
}
