package com.shoppingmall.cashshop.config;

import com.shoppingmall.cashshop.constant.Role;
import com.shoppingmall.cashshop.entity.Member;
import com.shoppingmall.cashshop.repository.MemberRepository;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Log
@Component
public class InitialMemberLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialMemberLoader(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(!memberRepository.existsByMemberEmail("admin@admin")){
            //관리자 계정 생성
            Member admin = new Member();
            admin.setMemberEmail("admin@admin");
            admin.setMemberName("admin");
            admin.setMemberPassword(passwordEncoder.encode("12345678"));
            admin.setMemberAddress("admin");
            admin.setMemberRole(Role.ADMIN);

            memberRepository.save(admin);
            log.info("ADMIN ACCOUNT CREATED: admin@admin / 12345678");
        } else {
            log.info("ADMIN ACCOUNT ALREADY EXISTS: admin@admin / 12345678");
        }
    }
}
