package com.shoppingmall.cashshop.entity;

import com.shoppingmall.cashshop.constant.Role;
import com.shoppingmall.cashshop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "member")
@Data @EqualsAndHashCode(callSuper = false)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long memberId;

    private String memberName;

    @Column(unique = true)
    private String memberEmail;

    private String memberPassword;
    private String memberAddress;

    @Enumerated(EnumType.STRING)
    private Role memberRole;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {

        Member member = new Member();
        member.setMemberName(memberFormDto.getMemberName());
        member.setMemberEmail(memberFormDto.getMemberEmail());
        member.setMemberAddress(memberFormDto.getMemberAddress());
        String memberPassword = passwordEncoder.encode(memberFormDto.getMemberPassword());
        member.setMemberPassword(memberPassword);
        member.setMemberRole(Role.USER);

        return member;
    }
}
