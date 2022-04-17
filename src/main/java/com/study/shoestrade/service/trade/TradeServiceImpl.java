package com.study.shoestrade.service.trade;

import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.SalesTradeSaveDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDtoInterface;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.product.ProductSizeNoSuchElementException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.product.ProductSizeRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;

    private final MemberRepository memberRepository;

    private final ProductSizeRepository productSizeRepository;

    /**
     * 판매 입찰 등록
     *
     * @param email             판매자 이메일
     * @param salesTradeSaveDto 판매 정보
     */
    @Transactional
    @Override
    public void salesTradeSave(String email, SalesTradeSaveDto salesTradeSaveDto) {
        log.info("info = {}", "TradeService - salesTradeSave 실행");

        tradeRepository.save(
                Trade.builder()
                        .price(salesTradeSaveDto.getPrice())
                        .productSize(productSizeRepository.findById(salesTradeSaveDto.getProductSizeId())
                                .orElseThrow(()->new ProductSizeNoSuchElementException(String.valueOf(salesTradeSaveDto.getProductSizeId())))) // 예외 추가 해야함
                        .seller(memberRepository.findByEmail(email)
                                .orElseThrow(MemberNotFoundException::new))
                        .tradeState(TradeState.SELL)
                        .tradeType(TradeType.SELL)
                        .build()
        );
    }

    /**
     * 사용자가 등록한 입찰 내역 검색
     *
     * @param email     사용자
     * @param tradeType 구매, 판매 구분
     * @param pageable  페이지
     * @return 검색된 입찰 내역
     */
    @Override
    public Page<TradeLoadDtoInterface> findTradeByEmailAndTradeType(String email, TradeType tradeType, Pageable pageable) {
        return tradeRepository.findTradeByEmailAndTradeType(memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new), tradeType, pageable);
    }
}
