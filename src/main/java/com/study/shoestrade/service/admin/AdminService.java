package com.study.shoestrade.service.admin;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.member.Role;
import com.study.shoestrade.domain.member.Token;
import com.study.shoestrade.domain.payment.Payment;
import com.study.shoestrade.domain.payment.PaymentStatus;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.dto.member.response.MemberDetailDto;
import com.study.shoestrade.dto.admin.PageMemberDto;
import com.study.shoestrade.exception.member.MemberNotFoundException;
import com.study.shoestrade.exception.payment.PaymentNotFoundException;
import com.study.shoestrade.exception.payment.PaymentUnpaidException;
import com.study.shoestrade.exception.token.InvalidRefreshTokenException;
import com.study.shoestrade.exception.trade.TradeEmptyResultDataAccessException;
import com.study.shoestrade.exception.trade.TradeNotCompletedException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.member.TokenRepository;
import com.study.shoestrade.repository.payment.PaymentRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import com.study.shoestrade.service.policy.grade.GradePolicy;
import com.study.shoestrade.service.policy.point.PointPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.study.shoestrade.domain.trade.TradeState.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class  AdminService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final TradeRepository tradeRepository;
    private final PaymentRepository paymentRepository;
    private final PointPolicy pointPolicy;
    private final GradePolicy gradePolicy;


    @Transactional(readOnly = true)
    public String getMemberEmail(Long id){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return member.getEmail();
    }

    // 회원 리스트 조회
    @Transactional(readOnly = true)
    public Page<PageMemberDto> getMembers(String email, Pageable pageable){
        return memberRepository.findMembers(email, pageable).map(PageMemberDto::create);
    }

    // 회원 상세 정보 조회
    @Transactional(readOnly = true)
    public MemberDetailDto getMemberDetail(Long id){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberDetailDto.create(member);
    }

    // 회원 정지
    public void banMember(Long memberId, int day){
        Member findMember = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        LocalDateTime now = LocalDateTime.now();

        if(day == -1){
            Token token = tokenRepository.findByMember(findMember.getId())
                    .orElseThrow(InvalidRefreshTokenException::new);
            tokenRepository.delete(token);
            memberRepository.delete(findMember);
        } else {
            findMember.changeRole(Role.BAN);
            findMember.updateBanReleaseTime(now.plusDays(day));
        }
    }

    // 회원 정지 해제
    public void releaseMember(Long memberId){
        Member findMember = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        findMember.changeRole(Role.ROLE_MEMBER);
        findMember.updateBanReleaseTime(LocalDateTime.now());
    }

    // 거래 상태 변경
    public void changeTradeState(Long tradeId, TradeState tradeState){
        Trade trade = tradeRepository.findTradeAndMembers(tradeId)
                .orElseThrow(() -> new TradeEmptyResultDataAccessException(tradeId.toString(), 1));

        checkTradeState(trade);

        if(tradeState.equals(DONE)){
            Member seller = trade.getSeller();
            Member purchaser = trade.getPurchaser();

            Payment payment = paymentRepository.findByTrade(trade).orElseThrow(PaymentNotFoundException::new);
            if(!payment.getStatus().equals(PaymentStatus.PAID)){
                throw new PaymentUnpaidException();
            }

            int savedPoint = pointPolicy.savePoint(purchaser, payment.getPrice());

            seller.addTradeCount();
            purchaser.addTradeCount();
            purchaser.addPoint(savedPoint);

            seller.upgradeGrade(gradePolicy.upgradeMemberGrade(seller));
            purchaser.upgradeGrade(gradePolicy.upgradeMemberGrade(purchaser));

            trade.finishTrade(LocalDateTime.now());
        }

        trade.changeState(tradeState);
    }

    private void checkTradeState(Trade trade) {
        TradeState tradeState = trade.getTradeState();

        if(tradeState == SELL || tradeState == PURCHASE || tradeState == READY || tradeState == FAIL){
            throw new TradeNotCompletedException();
        }
    }
}
