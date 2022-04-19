package com.study.shoestrade.service.trade;

import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.TradeDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
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
     * @param email    사용자 이메일
     * @param tradeDto 입찰 정보
     */
    @Transactional
    @Override
    public void TradeSave(String email, TradeDto tradeDto) {

        tradeRepository.save(
                Trade.builder()
                        .price(tradeDto.getPrice())
                        .productSize(productSizeRepository.findById(tradeDto.getProductSizeId())
                                .orElseThrow(() -> new ProductSizeNoSuchElementException(String.valueOf(tradeDto.getProductSizeId()))))
                        .seller(memberRepository.findByEmail(email)
                                .orElseThrow(MemberNotFoundException::new))
                        .tradeState(tradeDto.getTradeType() == TradeType.SELL ? TradeState.SELL : TradeState.PURCHASE)
                        .tradeType(tradeDto.getTradeType())
                        .build());
    }

    /**
     * 사용자가 등록한 입찰 내역 검색
     *
     * @param email     사용자 이메일
     * @param tradeType 구매, 판매 구분
     * @param pageable  페이지 정보
     * @return 검색된 입찰 내역
     */
    @Override
    public Page<TradeLoadDto> findTradeByEmailAndTradeType(String email, TradeType tradeType, Pageable pageable) {
        return tradeRepository.findTradeByEmailAndTradeType(email, tradeType, pageable);
    }

    /**
     * 입찰 금액 수정
     *
     * @param email    사용자 이메일
     * @param tradeDto 수정할 입찰 정보
     */
    @Transactional
    @Override
    public void updateTrade(String email, Long id, TradeDto tradeDto) {
        List<Trade> findTrade = tradeRepository.findByIdAndEmail(email, id, tradeDto.getTradeType());

        if (findTrade.isEmpty()) {
            throw new TradeEmptyResultDataAccessException(tradeDto.toString(), 1);
        }

        findTrade.get(0).changePrice(tradeDto.getPrice());
    }

    /**
     * 입찰 삭제
     *
     * @param email    사용자 이메일
     * @param tradeDto 삭제할 입찰 정보
     */
    @Transactional
    @Override
    public void deleteTrade(String email, TradeDto tradeDto) {
        List<Trade> findTrade = tradeRepository.findByIdAndEmail(email, tradeDto.getId(), tradeDto.getTradeType());

        if (findTrade.isEmpty()) {
            throw new TradeEmptyResultDataAccessException(tradeDto.toString(), 1);
        }

        tradeRepository.delete(findTrade.get(0));
    }
}
