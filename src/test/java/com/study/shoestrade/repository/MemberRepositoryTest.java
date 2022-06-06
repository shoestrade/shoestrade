package com.study.shoestrade.repository;

import com.study.shoestrade.domain.member.Member;
import com.study.shoestrade.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    Member member = Member.builder()
            .email("0tkk@naver.com")
            .password("1234")
            .shoeSize(255)
            .banCount(0)
            .warningCount(0)
            .build();


    @Test
    public void 회원_저장() {
        // given

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(memberRepository.findById(member.getId()).orElse(null)).isEqualTo(savedMember);
        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(member.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(member.getShoeSize()).isEqualTo(savedMember.getShoeSize());
    }

    @Test
    public void 회원_이메일_검색() {
        // given
        memberRepository.save(member);

        // when
        Member findByEmailMember = memberRepository.findByEmail(this.member.getEmail()).orElse(null);

        // then
        assertThat(findByEmailMember).isEqualTo(member);
    }

    @Test
    public void 회원_이메일_검색_실패() {
        // given
        memberRepository.save(member);

        // when
        Member findByEmailMember = memberRepository.findByEmail("ssssss@gmail.com").orElse(null);

        // then
        assertThat(findByEmailMember).isEqualTo(null);
    }

    @Test
    public void 회원_이메일_존재확인() {
        // given
        memberRepository.save(member);

        // when
        boolean existsByEmailTrue = memberRepository.existsByEmail(member.getEmail());
        boolean existsByEmailFalse = memberRepository.existsByEmail("ssssss@gmail.com");

        // then
        assertThat(existsByEmailTrue).isTrue();
        assertThat(existsByEmailFalse).isFalse();
    }
}