//package com.shoppingmall.cashshop;
//
//import com.shoppingmall.cashshop.dto.MemberFormDto;
//import com.shoppingmall.cashshop.entity.Member;
//import com.shoppingmall.cashshop.service.MemberService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
//import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
//import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
//
//@SpringBootTest
//@Transactional
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.properties")
//public class MemberControllerTest {
//
//    @Autowired
//    MemberService memberService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    // SecurityConfig에서 설정한 암호화 객체
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    public Member createMember(String email, String password){
//        MemberFormDto memberFormDto = new MemberFormDto();
//        memberFormDto.setMemberEmail(email);
//        memberFormDto.setMemberName("신창섭");
//        memberFormDto.setMemberAddress("정상시 다해구 바리동");
//        memberFormDto.setMemberPassword(password);
//        Member member = Member.createMember(memberFormDto, passwordEncoder);
//        return memberService.saveMember(member);
//    }
//
//    @Test
//    @DisplayName("로그인 성공 테스트")
//    public void loginSuccess() throws Exception {
//        String email = "test@test.com";
//        String password = "123456";
//        this.createMember(email, password);
//
//        mockMvc.perform(formLogin().loginProcessingUrl("/members/login")
//                .userParameter("email")
//                .user(email).password(password))
//                .andExpect(authenticated());
//    }
//
//    @Test
//    @DisplayName("로그인 실패 테스트")
//    public void loginFailure() throws Exception {
//        String email = "test@test.com";
//        String password = "123456";
//        this.createMember(email, password);
//
//        mockMvc.perform(formLogin().loginProcessingUrl("/members/login")
//                        .userParameter("email")
//                        .user(email).password("qwerqwer"))
//                .andExpect(unauthenticated());
//    }
//}
