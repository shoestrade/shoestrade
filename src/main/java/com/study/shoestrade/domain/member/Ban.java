package com.study.shoestrade.domain.member;

import java.util.Arrays;

public enum Ban {

    NOTBAN(0, 0),
    BAN1(1, 3),
    BAN2(2, 7),
    BAN3(3, 30),
    BAN4(4, -1);

    private int count;
    private int day;

    Ban(int count, int day) {
        this.count = count;
        this.day = day;
    }

    public static int getBanDay(int banCount){
        Ban ban = Arrays.stream(values())
                .filter(i -> i.count == banCount)
                .findFirst()
                .orElse(NOTBAN);

        return ban.day;
    }
}
