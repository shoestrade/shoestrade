package com.study.shoestrade.service.policy.point;

import com.study.shoestrade.domain.member.Grade;
import com.study.shoestrade.domain.member.Member;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.study.shoestrade.domain.member.Grade.*;

@Component
public class FirstPointPolicy implements PointPolicy {

    private Map<Grade, Integer> pointMap = Map.ofEntries(
            Map.entry(BRONZE, 0),
            Map.entry(SILVER, 1),
            Map.entry(GOLD, 3),
            Map.entry(PLATINUM, 5),
            Map.entry(DIAMOND, 8)
    );

    @Override
    public int savePoint(Member member, int price) {
        int savePercent = pointMap.get(member.getGrade());
        return price * savePercent / 100;
    }
}
