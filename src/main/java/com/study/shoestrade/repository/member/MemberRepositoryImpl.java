package com.study.shoestrade.repository.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.shoestrade.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.study.shoestrade.domain.member.QMember.member;
import static com.study.shoestrade.domain.member.Role.ROLE_ADMIN;

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
