package com.study.shoestrade.service.policy.point;

import com.study.shoestrade.domain.member.Member;

public interface PointPolicy {
    int savePoint(Member member, int price);
}
