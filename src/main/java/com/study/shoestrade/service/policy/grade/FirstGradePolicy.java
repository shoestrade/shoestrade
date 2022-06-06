package com.study.shoestrade.service.policy.grade;

import com.study.shoestrade.domain.member.Grade;
import com.study.shoestrade.domain.member.Member;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.TreeMap;

import static com.study.shoestrade.domain.member.Grade.*;

@Component
public class FirstGradePolicy implements GradePolicy {

    private TreeMap<Integer, Grade> gradeMap = new TreeMap<>(
            Map.ofEntries(
                    Map.entry(0, BRONZE),
                    Map.entry(5, SILVER),
                    Map.entry(15, GOLD),
                    Map.entry(30, PLATINUM),
                    Map.entry(100, DIAMOND)
            ));

    @Override
    @Transactional
    public Grade upgradeMemberGrade(Member member) {
        return gradeMap.floorEntry(member.getTradeCount()).getValue();
    }
}