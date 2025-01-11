package com.shoppingmall.cashshop.controller;

import com.shoppingmall.cashshop.dto.MemberFormDto;
import com.shoppingmall.cashshop.entity.Member;
import com.shoppingmall.cashshop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String newMember(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "members/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "폼 검증 중 오류가 발생했습니다.");
            return "members/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            System.out.println("Error Message: " + e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "members/memberForm";
        }


        return "redirect:/";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "members/login";
    }

    // 로그인 페이지 렌더링
    @GetMapping("/login")
    public String loginForm() {
        return "members/login"; // templates/members/login.html을 렌더링
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute String email,
                        @ModelAttribute String password, Model model) {
        // 로그인 로직 추가 (예: 이메일, 비밀번호 검증)
        boolean isValidUser = authenticate(email, password); // 인증 메서드 예시

        if (!isValidUser) {
            model.addAttribute("loginErrorMsg", "이메일 또는 비밀번호가 일치하지 않습니다.");
            return "redirect:/";
        }

        return "redirect:/"; // 로그인 성공 시 메인 페이지로
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

    private boolean authenticate(String email, String password) {
        // 실제 인증 로직 (DB 연동 등)
        return "user@example.com".equals(email) && "password".equals(password);
    }



}
