//package com.shoppingmall.cashshop;
//
//import com.shoppingmall.cashshop.dto.MemberFormDto;
//import com.shoppingmall.cashshop.entity.Member;
//import com.shoppingmall.cashshop.service.MemberService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
//public class MemberServiceTest {
//
//    @Autowired
//    MemberService memberService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    //Controller에서 POST 입력 값을 기반으로 memberFormDto 객체를 생성
//    public Member createMember(){
//        MemberFormDto memberFormDto = new MemberFormDto();
//        memberFormDto.setMemberEmail("test@email.com");
//        memberFormDto.setMemberName("신창섭");
//        memberFormDto.setMemberAddress("정상시 다해구 바리동");
//        memberFormDto.setMemberPassword("1234");
//        return Member.createMember(memberFormDto, passwordEncoder);
//    }
//
//    @Test
//    @DisplayName("회원가입 테스트")
//    public void saveMemberTest(){
//        Member member = createMember();
//        Member savedMember = memberService.saveMember(member);
//
//        assertEquals(member.getMemberEmail(), savedMember.getMemberEmail());
//        assertEquals(member.getMemberName(), savedMember.getMemberName());
//        assertEquals(member.getMemberAddress(), savedMember.getMemberAddress());
//        assertEquals(member.getMemberPassword(), savedMember.getMemberPassword());
//        assertEquals(member.getMemberRole(), savedMember.getMemberRole());
//    }
//
//    @Test
//    @DisplayName("중복 가입 테스트")
//    void validateDuplicateMemberTest(){
//        //given
//        Member member1 = createMember();
//        Member member2 = createMember();
//        memberService.saveMember(member1);
//
//        //when
//        Throwable e = assertThrows(IllegalStateException.class, ()->{
//            memberService.saveMember(member2);
//        });
//
//        //then
//        assertEquals("이미 가입된 이메일입니다.", e.getMessage());
//
//    }
//
//
//}
