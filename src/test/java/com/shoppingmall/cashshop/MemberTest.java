package com.shoppingmall.cashshop;

import com.shoppingmall.cashshop.entity.Member;
import com.shoppingmall.cashshop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing test")
    @WithMockUser(username = "test", roles = "USER")
    public void auditingTest() {
        Member member_test = new Member();
        memberRepository.save(member_test);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(member_test.getMemberId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("register time: " + member.getRegTime());
        System.out.println("update date: " + member.getUpdateTime());
        System.out.println("create member: " + member.getCreatedBy());
        System.out.println("modify member: " + member.getModifiedBy());
    }
}
