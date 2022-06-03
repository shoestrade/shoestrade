package com.study.shoestrade.service;

import com.study.shoestrade.domain.member.Grade;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.member.Token;
import com.study.shoestrade.domain.payment.Payment;
import com.study.shoestrade.domain.payment.PaymentStatus;
import com.study.shoestrade.domain.trade.Trade;
import com.study.shoestrade.domain.trade.TradeState;
import com.study.shoestrade.domain.trade.TradeType;
import com.study.shoestrade.dto.admin.PageMemberDto;
import com.study.shoestrade.dto.member.response.MemberDetailDto;
import com.study.shoestrade.exception.payment.PaymentNotFoundException;
import com.study.shoestrade.exception.payment.PaymentUnpaidException;
import com.study.shoestrade.exception.trade.TradeNotCompletedException;
import com.study.shoestrade.repository.member.MemberRepository;
import com.study.shoestrade.repository.member.TokenRepository;
import com.study.shoestrade.repository.payment.PaymentRepository;
import com.study.shoestrade.repository.trade.TradeRepository;
import com.study.shoestrade.service.admin.AdminService;
import com.study.shoestrade.service.policy.grade.GradePolicy;
import com.study.shoestrade.service.policy.point.PointPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.study.shoestrade.domain.member.Role.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    AdminService adminService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    TokenRepository tokenRepository;
    @Mock
    TradeRepository tradeRepository;
    @Mock
    PaymentRepository paymentRepository;
    @Mock
    PointPolicy pointPolicy;
    @Mock
    GradePolicy gradePolicy;

    List<Member> members;

    @BeforeEach
    public void init(){
        Member member1 = Member.builder()
                .id(1L)
                .email("aaa")
                .name("aaa")
                .phone("01011111111")
                .point(123)
                .tradeCount(0)
                .shoeSize(250)
                .grade(Grade.BRONZE)
                .role(ROLE_MEMBER)
                .banReleaseTime(LocalDateTime.of(2022, 4, 18, 13, 13, 13))
                .build();

        Member member2 = Member.builder()
                .id(2L)
                .email("bbb")
                .name("bbb")
                .phone("01022222222")
                .point(29)
                .tradeCount(5)
                .shoeSize(255)
                .grade(Grade.SILVER)
                .role(ROLE_MEMBER)
                .banReleaseTime(LocalDateTime.now())
                .build();

        Member member3 = Member.builder()
                .id(3L)
                .email("ccc")
                .name("ccc")
                .phone("01033333333")
                .point(0)
                .shoeSize(260)
                .role(BAN)
                .banReleaseTime(LocalDateTime.now())
                .build();

        members = new ArrayList<>();
        members.add(member1);
        members.add(member2);
        members.add(member3);
    }

    @Test
    public void 회원_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 1);
        Page<Member> page = new PageImpl<>(members, pageable, 3L);

        // mocking
        given(memberRepository.findMembers(any(), any())).willReturn(page);

        // when
        Page<PageMemberDto> responseDto = adminService.getMembers("", pageable);

        // then
        assertThat(responseDto.getContent().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("관리자가 기간을 설정하고 정지 요청하면 회원 정지에 성공한다.")
    public void 회원_정지_성공1() {
        // given
        Member member = members.get(1);
        LocalDateTime time = member.getBanReleaseTime();
        assertThat(member.getRole()).isEqualTo(ROLE_MEMBER);

        // mocking
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        // when
        adminService.banMember(1L, 3);

        // then
        assertThat(member.getRole()).isEqualTo(BAN);
        assertThat(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(member.getBanReleaseTime()))
                .isEqualTo(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(time.plusDays(3)));
    }

    @Test
    @DisplayName("정지 기간을 -1로 설정하면 회원을 탈퇴시킨다.")
    public void 회원_정지_성공2() {
        // given
        Member member = members.get(1);
        Token token = Token.builder()
                .id(1L)
                .refreshToken("token")
                .build();

        // mocking
        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        given(tokenRepository.findByMember(any())).willReturn(Optional.of(token));

        // when, then
        assertThatCode(() ->  adminService.banMember(1L, -1))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("회원 정지 해제에 성공한다.")
    public void 회원_정지_해제_성공() {
        // given
        Member member = members.get(2);
        assertThat(member.getRole()).isEqualTo(BAN);

        // mocking
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        // when
        adminService.releaseMember(2L);

        // then
        assertThat(member.getRole()).isEqualTo(ROLE_MEMBER);
    }

    @Test
    @DisplayName("관리자가 회원의 email로 회원의 상세정보를 조회할 수 있다.")
    public void 회원_상세_정보_조회() {
        // given
        Member member = members.get(0);

        // mocking
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        // when
        MemberDetailDto responseDto = adminService.getMemberDetail(1L);

        // then
        assertThat(responseDto.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("관리자가 거래의 상태를 할 수 있다.")
    public void 거래_상태_변경_성공1() {
        // given
        Trade trade = Trade.builder()
                .id(11L)
                .price(10000)
                .tradeType(TradeType.PURCHASE)
                .tradeState(TradeState.CENTER_DELIVERY)
                .seller(members.get(0))
                .purchaser(members.get(1))
                .build();

        // mocking
        given(tradeRepository.findTradeAndMembers(trade.getId())).willReturn(Optional.of(trade));

        // when
        adminService.changeTradeState(trade.getId(), TradeState.REAL);

        // then
        assertThat(trade.getTradeState()).isEqualTo(TradeState.REAL);
    }

    @Test
    @DisplayName("거래가 완료되어 상태가 DONE이 되면 회원의 tradeCount가 증가한다.")
    public void 거래_상태_변경_성공2() {
        // given
        Trade trade = Trade.builder()
                .id(11L)
                .price(10000)
                .tradeType(TradeType.PURCHASE)
                .tradeState(TradeState.COMPLETE)
                .seller(members.get(0))
                .purchaser(members.get(1))
                .build();

        Payment payment = Payment.builder()
                .id(21L)
                .trade(trade)
                .price(10000)
                .point(0)
                .status(PaymentStatus.PAID)
                .build();

        // mocking
        given(tradeRepository.findTradeAndMembers(trade.getId())).willReturn(Optional.of(trade));
        given(paymentRepository.findByTrade(trade)).willReturn(Optional.of(payment));

        // when
        adminService.changeTradeState(trade.getId(), TradeState.DONE);

        // then
        assertThat(members.get(0).getTradeCount()).isEqualTo(1);
        assertThat(members.get(1).getTradeCount()).isEqualTo(6);
        assertThat(trade.getTradeCompletionDate()).isNotNull();
        assertThat(trade.getTradeState()).isEqualTo(TradeState.DONE);
    }

    @Test
    @DisplayName("거래가 완료되면 포인트가 적립된다.")
    public void 거래_상태_변경_성공3() {
        // given
        Trade trade = Trade.builder()
                .id(11L)
                .price(10000)
                .tradeType(TradeType.PURCHASE)
                .tradeState(TradeState.COMPLETE)
                .seller(members.get(0))
                .purchaser(members.get(1))
                .build();

        Payment payment = Payment.builder()
                .id(21L)
                .trade(trade)
                .price(9900)
                .point(100)
                .status(PaymentStatus.PAID)
                .build();

        // mocking
        given(tradeRepository.findTradeAndMembers(trade.getId())).willReturn(Optional.of(trade));
        given(paymentRepository.findByTrade(trade)).willReturn(Optional.of(payment));
        given(pointPolicy.savePoint(members.get(1), payment.getPrice())).willReturn(9900 * 1 / 100);

        // when
        adminService.changeTradeState(trade.getId(), TradeState.DONE);

        // then
        assertThat(members.get(0).getTradeCount()).isEqualTo(1);
        assertThat(members.get(1).getTradeCount()).isEqualTo(6);
        assertThat(members.get(1).getPoint()).isEqualTo(29 + 9900 * 1 / 100);
        assertThat(trade.getTradeCompletionDate()).isNotNull();
        assertThat(trade.getTradeState()).isEqualTo(TradeState.DONE);
    }

    @Test
    @DisplayName("거래가 아직 결제가 되지 않으면 관리자는 거래 상태를 변경할 수 없다.")
    public void 거래_상태_변경_실패1() {
        // given
        Trade trade = Trade.builder()
                .id(11L)
                .price(10000)
                .tradeType(TradeType.PURCHASE)
                .tradeState(TradeState.READY)
                .seller(members.get(0))
                .purchaser(members.get(1))
                .build();

        // mocking
        given(tradeRepository.findTradeAndMembers(trade.getId())).willReturn(Optional.of(trade));

        // when, then
        assertThatThrownBy(() -> adminService.changeTradeState(trade.getId(), TradeState.DONE))
                .isInstanceOf(TradeNotCompletedException.class);
    }

    @Test
    @DisplayName("실패한 거래는 관리자는 거래 상태를 변경할 수 없다.")
    public void 거래_상태_변경_실패2() {
        // given
        Trade trade = Trade.builder()
                .id(11L)
                .price(10000)
                .tradeType(TradeType.SELL)
                .tradeState(TradeState.FAIL)
                .seller(members.get(0))
                .purchaser(members.get(1))
                .build();

        // mocking
        given(tradeRepository.findTradeAndMembers(trade.getId())).willReturn(Optional.of(trade));

        // when, then
        assertThatThrownBy(() -> adminService.changeTradeState(trade.getId(), TradeState.SELL))
                .isInstanceOf(TradeNotCompletedException.class);
    }

    @Test
    @DisplayName("결제 정보를 찾을 수 없으면 PaymentNotFoundException 예외가 발생한다.")
    public void 거래_상태_변경_실패3() {
        // given
        Trade trade = Trade.builder()
                .id(11L)
                .price(10000)
                .tradeType(TradeType.PURCHASE)
                .tradeState(TradeState.COMPLETE)
                .seller(members.get(0))
                .purchaser(members.get(1))
                .build();

        // mocking
        given(tradeRepository.findTradeAndMembers(trade.getId())).willReturn(Optional.of(trade));
        given(paymentRepository.findByTrade(trade)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> adminService.changeTradeState(trade.getId(), TradeState.DONE))
                .isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    @DisplayName("거래에 대한 결제가 이루어지지 않았으면 ")
    public void 거래_상태_변경_실패4() {
        // given
        Trade trade = Trade.builder()
                .id(11L)
                .price(10000)
                .tradeType(TradeType.PURCHASE)
                .tradeState(TradeState.COMPLETE)
                .seller(members.get(0))
                .purchaser(members.get(1))
                .build();

        Payment payment = Payment.builder()
                .id(21L)
                .trade(trade)
                .price(9900)
                .point(100)
                .status(PaymentStatus.READY)
                .build();

        // mocking
        given(tradeRepository.findTradeAndMembers(trade.getId())).willReturn(Optional.of(trade));
        given(paymentRepository.findByTrade(trade)).willReturn(Optional.of(payment));

        // when, then
        assertThatThrownBy(() -> adminService.changeTradeState(trade.getId(), TradeState.DONE))
                .isInstanceOf(PaymentUnpaidException.class);
    }

}