package com.study.shoestrade.service.policyTest.grade;

import com.study.shoestrade.domain.member.Grade;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.service.policy.grade.FirstGradePolicy;
import com.study.shoestrade.service.policy.grade.GradePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.study.shoestrade.domain.member.Grade.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FirstGradePolicyTest {

    private GradePolicy gradePolicy = new FirstGradePolicy();

    @Test
    @DisplayName("회원 거래량이 5회 미만이면 회원 등급이 BRONZE가 된다.")
    public void 등급_변경_브론즈() {
        // given
        Member member1 = Member.builder()
                .id(1L)
                .tradeCount(0)
                .grade(BRONZE)
                .build();

        Member member2 = Member.builder()
                .id(2L)
                .tradeCount(3)
                .grade(BRONZE)
                .build();

        Member member3 = Member.builder()
                .id(3L)
                .tradeCount(4)
                .grade(BRONZE)
                .build();

        Member member4 = Member.builder()
                .id(4L)
                .tradeCount(5)
                .grade(BRONZE)
                .build();

        // when, then
        assertThat(gradePolicy.upgradeMemberGrade(member1)).isEqualTo(BRONZE);
        assertThat(gradePolicy.upgradeMemberGrade(member2)).isEqualTo(BRONZE);
        assertThat(gradePolicy.upgradeMemberGrade(member3)).isEqualTo(BRONZE);
        assertThat(gradePolicy.upgradeMemberGrade(member4)).isEqualTo(SILVER);
    }

    @Test
    @DisplayName("회원 거래량이 5회 이상 15회 미만이면 회원 등급이 SILVER가 된다.")
    public void 등급_변경_실버() {
        // given
        Member member1 = Member.builder()
                .id(1L)
                .tradeCount(4)
                .grade(BRONZE)
                .build();

        Member member2 = Member.builder()
                .id(2L)
                .tradeCount(5)
                .grade(BRONZE)
                .build();

        Member member3 = Member.builder()
                .id(3L)
                .tradeCount(10)
                .grade(BRONZE)
                .build();

        Member member4 = Member.builder()
                .id(4L)
                .tradeCount(14)
                .grade(BRONZE)
                .build();

        Member member5 = Member.builder()
                .id(5L)
                .tradeCount(15)
                .grade(SILVER)
                .build();

        // when, then
        assertThat(gradePolicy.upgradeMemberGrade(member1)).isEqualTo(BRONZE);
        assertThat(gradePolicy.upgradeMemberGrade(member2)).isEqualTo(SILVER);
        assertThat(gradePolicy.upgradeMemberGrade(member3)).isEqualTo(SILVER);
        assertThat(gradePolicy.upgradeMemberGrade(member4)).isEqualTo(SILVER);
        assertThat(gradePolicy.upgradeMemberGrade(member5)).isEqualTo(GOLD);
    }

    @Test
    @DisplayName("회원 거래량이 15회 이상 30회 미만이면 회원 등급이 GOLD가 된다.")
    public void 등급_변경_골드() {
        // given
        Member member1 = Member.builder()
                .id(1L)
                .tradeCount(14)
                .grade(SILVER)
                .build();

        Member member2 = Member.builder()
                .id(2L)
                .tradeCount(15)
                .grade(SILVER)
                .build();

        Member member3 = Member.builder()
                .id(3L)
                .tradeCount(24)
                .grade(SILVER)
                .build();

        Member member4 = Member.builder()
                .id(4L)
                .tradeCount(29)
                .grade(SILVER)
                .build();

        Member member5 = Member.builder()
                .id(5L)
                .tradeCount(30)
                .grade(GOLD)
                .build();

        // when, then
        assertThat(gradePolicy.upgradeMemberGrade(member1)).isEqualTo(SILVER);
        assertThat(gradePolicy.upgradeMemberGrade(member2)).isEqualTo(GOLD);
        assertThat(gradePolicy.upgradeMemberGrade(member3)).isEqualTo(GOLD);
        assertThat(gradePolicy.upgradeMemberGrade(member4)).isEqualTo(GOLD);
        assertThat(gradePolicy.upgradeMemberGrade(member5)).isEqualTo(PLATINUM);
    }

    @Test
    @DisplayName("회원 거래량이 30회 이상 100회 미만이면 회원 등급이 PLATINUM이 된다.")
    public void 등급_변경_플래티넘() {
        // given
        Member member1 = Member.builder()
                .id(1L)
                .tradeCount(29)
                .grade(GOLD)
                .build();

        Member member2 = Member.builder()
                .id(2L)
                .tradeCount(30)
                .grade(GOLD)
                .build();

        Member member3 = Member.builder()
                .id(3L)
                .tradeCount(59)
                .grade(GOLD)
                .build();

        Member member4 = Member.builder()
                .id(4L)
                .tradeCount(99)
                .grade(GOLD)
                .build();

        Member member5 = Member.builder()
                .id(5L)
                .tradeCount(100)
                .grade(PLATINUM)
                .build();

        // when, then
        assertThat(gradePolicy.upgradeMemberGrade(member1)).isEqualTo(GOLD);
        assertThat(gradePolicy.upgradeMemberGrade(member2)).isEqualTo(PLATINUM);
        assertThat(gradePolicy.upgradeMemberGrade(member3)).isEqualTo(PLATINUM);
        assertThat(gradePolicy.upgradeMemberGrade(member4)).isEqualTo(PLATINUM);
        assertThat(gradePolicy.upgradeMemberGrade(member5)).isEqualTo(DIAMOND);
    }

    @Test
    @DisplayName("회원 거래량이 100회 이상이면 회원 등급이 DIAMOND가 된다.")
    public void 등급_변경_다이아몬드() {
        // given
        Member member1 = Member.builder()
                .id(1L)
                .tradeCount(99)
                .grade(PLATINUM)
                .build();

        Member member2 = Member.builder()
                .id(2L)
                .tradeCount(100)
                .grade(DIAMOND)
                .build();

        Member member3 = Member.builder()
                .id(3L)
                .tradeCount(150)
                .grade(DIAMOND)
                .build();

        // when, then
        assertThat(gradePolicy.upgradeMemberGrade(member1)).isEqualTo(PLATINUM);
        assertThat(gradePolicy.upgradeMemberGrade(member2)).isEqualTo(DIAMOND);
        assertThat(gradePolicy.upgradeMemberGrade(member3)).isEqualTo(DIAMOND);
    }
}
