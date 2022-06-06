package com.study.shoestrade.service.policy.grade;

import com.study.shoestrade.domain.member.Grade;
import com.study.shoestrade.domain.member.Member;

public interface GradePolicy {
    Grade upgradeMemberGrade(Member member);
}
