package com.study.shoestrade.service.trade;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.trade.request.TradeDto;
import com.study.shoestrade.dto.trade.response.TradeBreakdownCountDto;
import com.study.shoestrade.dto.trade.response.TradeDoneDto;
import com.study.shoestrade.dto.trade.response.TradeLoadDto;
import com.study.shoestrade.dto.trade.response.TradeTransactionDto;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.payment.MyTradeException;
import com.study.shoestrade.exception.product.ProductEmptyResultDataAccessException;
import com.study.shoestrade.exception.product.ProductSizeNoSuchElementException;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.exception.trade.WrongStateException;
import com.study.shoestrade.exception.trade.WrongTradeTypeException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.product.ProductRepository;
import com.study.shoestrade.repository.product.ProductSizeRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import com.study.shoestrade.service.member.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;

    private final ProductRepository productRepository;

    private final MemberRepository memberRepository;

    private final ProductSizeRepository productSizeRepository;
    private final MailService mailService;

    /**
     * ?????? ??????
     *
     * @param email    ????????? ?????????
     * @param tradeDto ?????? ??????
     */
    @Transactional
    @Override
    public void TradeSave(String email, TradeDto tradeDto) {

        tradeRepository.save(
                Trade.builder()
                        .price(tradeDto.getPrice())
                        .productSize(productSizeRepository.findById(tradeDto.getProductSizeId())
                                .orElseThrow(() -> new ProductSizeNoSuchElementException(String.valueOf(tradeDto.getProductSizeId()))))
                        .purchaser(tradeDto.getTradeType() == TradeType.SELL ? null : memberRepository.findByEmail(email)
                                .orElseThrow(MemberNotFoundException::new))
                        .seller(tradeDto.getTradeType() == TradeType.SELL ? memberRepository.findByEmail(email)
                                .orElseThrow(MemberNotFoundException::new) : null)
                        .tradeState(tradeDto.getTradeType() == TradeType.SELL ? TradeState.SELL : TradeState.PURCHASE)
                        .tradeType(tradeDto.getTradeType())
                        .build());
    }

    // ?????? ?????? ??? ??????
    @Override
    public TradeBreakdownCountDto getBreakdownCount(String email, String tradeType){
        return tradeRepository.findBreakdownCount(email, getTradeType(tradeType));
    }

    // ?????? ?????? ??????
    @Override
    public Page<TradeLoadDto> getBreakdown(String email, String tradeType, String state, Pageable pageable) {
        checkState(state);
        return tradeRepository.findBreakdown(email, getTradeType(tradeType), state, pageable);
    }

    /**
     * ?????? ?????? ??????
     *
     * @param email    ????????? ?????????
     * @param tradeDto ????????? ?????? ??????
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
     * ?????? ??????
     *
     * @param email    ????????? ?????????
     * @param tradeDto ????????? ?????? ??????
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

    /**
     * ????????? ?????? ?????? ??????
     *
     * @param productId ?????? id
     * @param pageable  ????????? ??????
     * @return ?????? ??????
     */
    @Override
    public Page<TradeDoneDto> findDoneTrade(Long productId, Pageable pageable) {
        productRepository.findById(productId).orElseThrow(() ->
                new ProductEmptyResultDataAccessException(productId.toString(), 1)
        );

        return tradeRepository.findDoneTrade(productId, pageable);
    }

    /**
     * ????????? ?????? ??????
     *
     * @param productId  ?????? id
     * @param tradeState ?????? ??????(??????, ??????)
     * @param pageable   ????????? ??????
     * @return ?????? ??????
     */
    @Override
    public Page<TradeTransactionDto> findTransactionTrade(Long productId, String tradeState, Pageable pageable) {
        productRepository.findById(productId).orElseThrow(() ->
                new ProductEmptyResultDataAccessException(productId.toString(), 1)
        );
        return tradeRepository.findTransactionTrade(productId, getTradeState(tradeState), pageable);
    }

    /**
     * ?????? ?????????
     *
     * @param productId  ?????? id
     * @param tradeState ?????? ??????(??????, ??????)
     * @return ?????? ??????
     */
    @Override
    public List<TradeLoadDto> findInstantTrade(Long productId, String tradeState) {
        productRepository.findById(productId).orElseThrow(() ->
                new ProductEmptyResultDataAccessException(productId.toString(), 1)
        );
        return tradeRepository.findInstantTrade(productId, getTradeState(tradeState));
    }

    // ?????? ?????? ??????
    private TradeType getTradeType(String tradeType) {
        if(tradeType.equals("sell")) return TradeType.SELL;
        else if(tradeType.equals("purchase")) return TradeType.PURCHASE;

        throw new WrongTradeTypeException(tradeType);
    }

    // ?????? ?????? ??????
    private TradeState getTradeState(String tradeState) {
        if(tradeState.equals("sell")) return TradeState.PURCHASE;
        else if(tradeState.equals("purchase")) return TradeState.SELL;

        throw new WrongStateException(tradeState);
    }

    // ???????????? ??????, ?????? ???, ???????????? ??????
    private void checkState(String state){
        if(!state.equals("bid") && !state.equals("progress") && !state.equals("done")){
            throw new WrongStateException(state);
        }
    }

    // ?????? ?????? ??????
    @Override
    @Transactional
    public void sellTrade(String email, Long tradeId){
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(() -> new TradeEmptyResultDataAccessException(tradeId.toString(), 1));

        checkTradeStateAndTradeType(trade);

        if(trade.getPurchaser().equals(member)){
            throw new MyTradeException();
        }

        LocalDate now = LocalDate.now();
        LocalDateTime deadline = LocalDateTime.of(now.plusDays(2), LocalTime.MAX.minusSeconds(1));

        trade.changeState(TradeState.READY);
        trade.changeSeller(member);
        trade.changeClaimDueDate(deadline);

        mailService.sendClaimMail(trade.getPurchaser().getEmail(), deadline);
    }

    private void checkTradeStateAndTradeType(Trade trade) {
        if(!trade.getTradeState().equals(TradeState.PURCHASE) || !trade.getTradeType().equals(TradeType.PURCHASE)){
            throw new WrongTradeTypeException();
        }
    }

}
