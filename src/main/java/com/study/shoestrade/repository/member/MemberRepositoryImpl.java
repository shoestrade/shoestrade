package com.study.shoestrade.repository.member;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.interest.QInterestProduct;
import com.study.shoestrade.domain.member.Address;
import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.domain.product.QProduct;
import com.study.shoestrade.domain.product.QProductSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.study.shoestrade.domain.interest.QInterestProduct.*;
import static com.study.shoestrade.domain.member.QAccount.*;
import static com.study.shoestrade.domain.member.QAddress.*;
import static com.study.shoestrade.domain.member.QMember.member;
import static com.study.shoestrade.domain.member.Role.ROLE_ADMIN;
import static com.study.shoestrade.domain.product.QProduct.product;
import static com.study.shoestrade.domain.product.QProductSize.productSize;
import static com.study.shoestrade.domain.trade.QTrade.*;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Member> findMembers(String email, Pageable pageable) {
        List<Member> content = queryFactory
                .selectFrom(member)
                .where(emailEq(email),
                        member.role.ne(ROLE_ADMIN))
                .orderBy(member.email.asc())
                .offset(pageable.getOffset())
                .limit(10)
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }

    private BooleanExpression emailEq(String email){
        return email == null ? null : member.email.contains(email);
    }

}
