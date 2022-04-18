package com.study.shoestrade.service.trade;

import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.TradeSaveDto;
import com.study.shoestrade.dto.trade.request.TradeDeleteDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import com.study.shoestrade.dto.trade.request.TradeUpdateDto;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.product.ProductSizeNoSuchElementException;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.product.ProductSizeRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;

    private final MemberRepository memberRepository;

    private final ProductSizeRepository productSizeRepository;

    /**
     * 입찰 등록
     *
     * @param email        사용자 이메일
     * @param tradeSaveDto 입찰 정보
     */
    @Transactional
    @Override
    public void TradeSave(String email, TradeSaveDto tradeSaveDto) {

        tradeRepository.save(
                Trade.builder()
                .price(tradeSaveDto.getPrice())
                .productSize(productSizeRepository.findById(tradeSaveDto.getProductSizeId())
                        .orElseThrow(() -> new ProductSizeNoSuchElementException(String.valueOf(tradeSaveDto.getProductSizeId()))))
                .seller(memberRepository.findByEmail(email)
                        .orElseThrow(MemberNotFoundException::new))
                .tradeState(tradeSaveDto.getTradeType() == TradeType.SELL ? TradeState.SELL : TradeState.PURCHASE)
                .tradeType(tradeSaveDto.getTradeType())
                .build());
    }

    /**
     * 사용자가 등록한 입찰 내역 검색
     *
     * @param email     사용자 이메일
     * @param tradeType 구매, 판매 구분
     * @param pageable  페이지
     * @return 검색된 입찰 내역
     */
    @Override
    public Page<TradeLoadDto> findTradeByEmailAndTradeType(String email, TradeType tradeType, Pageable pageable) {
        return tradeRepository.findTradeByEmailAndTradeType(email, tradeType, pageable);
    }

    /**
     * 입찰 금액 수정
     *
     * @param email          사용자 이메일
     * @param tradeUpdateDto 수정할 입찰 정보
     */
    @Transactional
    @Override
    public void updateTrade(String email, TradeUpdateDto tradeUpdateDto) {
        List<Trade> findTrade = tradeRepository.findByIdAndEmail(email, tradeUpdateDto.getId(), tradeUpdateDto.getTradeType());

        if (findTrade.isEmpty()) {
            throw new TradeEmptyResultDataAccessException(tradeUpdateDto.toString(), 1);
        }

        findTrade.get(0).changePrice(tradeUpdateDto.getPrice());
    }

    /**
     * 입찰 삭제
     *
     * @param email          사용자 이메일
     * @param tradeDeleteDto 삭제할 입찰 정보
     */
    @Transactional
    @Override
    public void deleteTrade(String email, TradeDeleteDto tradeDeleteDto) {
        List<Trade> findTrade = tradeRepository.findByIdAndEmail(email, tradeDeleteDto.getId(), tradeDeleteDto.getTradeType());

        if (findTrade.isEmpty()) {
            throw new TradeEmptyResultDataAccessException(tradeDeleteDto.toString(), 1);
        }

        tradeRepository.delete(findTrade.get(0));
    }
}
