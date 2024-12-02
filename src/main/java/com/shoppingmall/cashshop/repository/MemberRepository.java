package com.shoppingmall.cashshop.repository;

import com.shoppingmall.cashshop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberEmail(String memberEmail);

    boolean existsByMemberEmail(String memberEmail);
}
