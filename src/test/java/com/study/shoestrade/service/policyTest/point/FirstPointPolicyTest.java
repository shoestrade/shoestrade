package com.study.shoestrade.service.policyTest.point;

import com.study.shoestrade.domain.member.Grade;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.member.Role;
import com.study.shoestrade.service.policy.point.FirstPointPolicy;
import com.study.shoestrade.service.policy.point.PointPolicy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FirstPointPolicyTest {

    private PointPolicy pointPolicy = new FirstPointPolicy();

    @Test
    @DisplayName("회원 등급이 BRONZE이면 포인트는 적립되지 않습니다.")
    public void 포인트_적립_성공_브론즈() {
        // given
        Member purchaser = Member.builder()
                .id(1L)
                .email("email")
                .name("purchaser")
                .point(0)
                .tradeCount(2)
                .grade(Grade.BRONZE)
                .role(Role.ROLE_MEMBER)
                .build();

        // when
        int savedPoint = pointPolicy.savePoint(purchaser, 10000);

        // then
        assertThat(savedPoint).isEqualTo(0);
    }

    @Test
    @DisplayName("회원 등급이 SILVER이면 포인트가 가격의 1%가 적립된다.")
    public void 포인트_적립_성공_실버() {
        // given
        Member purchaser = Member.builder()
                .id(1L)
                .email("email")
                .name("purchaser")
                .point(123)
                .tradeCount(5)
                .grade(Grade.SILVER)
                .role(Role.ROLE_MEMBER)
                .build();

        // when
        int savedPoint = pointPolicy.savePoint(purchaser, 10000);

        // then
        assertThat(savedPoint).isEqualTo(10000 * 1 / 100);
    }

    @Test
    @DisplayName("회원 등급이 GOLD이면 포인트가 가격의 3%가 적립된다.")
    public void 포인트_적립_성공_골드() {
        // given
        Member purchaser = Member.builder()
                .id(1L)
                .email("email")
                .name("purchaser")
                .point(123)
                .tradeCount(17)
                .grade(Grade.GOLD)
                .role(Role.ROLE_MEMBER)
                .build();

        // when
        int savedPoint = pointPolicy.savePoint(purchaser, 10000);

        // then
        assertThat(savedPoint).isEqualTo(10000 * 3 / 100);
    }

    @Test
    @DisplayName("회원 등급이 PLATINUM이면 포인트가 가격의 5%가 적립된다.")
    public void 포인트_적립_성공_플래티넘() {
        // given
        Member purchaser = Member.builder()
                .id(1L)
                .email("email")
                .name("purchaser")
                .point(123)
                .tradeCount(50)
                .grade(Grade.PLATINUM)
                .role(Role.ROLE_MEMBER)
                .build();

        // when
        int savedPoint = pointPolicy.savePoint(purchaser, 10000);

        // then
        assertThat(savedPoint).isEqualTo(10000 * 5 / 100);
    }

    @Test
    @DisplayName("회원 등급이 DIAMOND이면 포인트가 가격의 3%가 적립된다.")
    public void 포인트_적립_성공_다이아몬드() {
        // given
        Member purchaser = Member.builder()
                .id(1L)
                .email("email")
                .name("purchaser")
                .point(123)
                .tradeCount(200)
                .grade(Grade.DIAMOND)
                .role(Role.ROLE_MEMBER)
                .build();

        // when
        int savedPoint = pointPolicy.savePoint(purchaser, 10000);

        // then
        assertThat(savedPoint).isEqualTo(10000 * 8 / 100);
    }
}
