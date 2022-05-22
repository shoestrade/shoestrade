package com.study.shoestrade.service.scheduler;

import com.study.shoestrade.domain.member.Ban;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.member.Token;
import com.study.shoestrade.dto.scheduler.OverdueMember;
import com.study.shoestrade.exception.token.InvalidRefreshTokenException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.member.TokenRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SchedulerService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    @Scheduled(cron = "00 00 00 * * *", zone = "Asia/Seoul")
    public void schedulerTest() {
        LocalDateTime now = LocalDateTime.now();
        List<Long> overdueTrades = tradeRepository.findOverdueTrade(now);
        List<OverdueMember> overdueMembers = tradeRepository.findOverdueMember(now);

        log.info("list = {}", overdueTrades);

        tradeRepository.updateTradeStatesFromReadyToFail(overdueTrades);
        overdueMembers.forEach(i -> memberRepository.updateMemberWaringCount(i.getCount(), i.getMemberId()));

        List<Long> memberIds = overdueMembers.stream()
                .map(OverdueMember::getMemberId)
                .collect(Collectors.toList());

        List<Member> warnedMembers = memberRepository.findWarnedMembers(memberIds);
        warnedMembers.stream()
                .map(member -> member.banningMember(Ban.getBanDay(Math.min((int)(member.getWarningCount() / 3), 4)), now))
                .filter(member -> Ban.getBanDay(Math.min((int)(member.getWarningCount() / 3), 4)) == -1)
                .forEach(member -> {
                    Token token = tokenRepository.findByMember(member.getId()).orElseThrow(InvalidRefreshTokenException::new);
                    tokenRepository.delete(token);
                    memberRepository.delete(member);
                });
    }
}
