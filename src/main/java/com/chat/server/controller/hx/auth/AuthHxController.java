package com.chat.server.controller.hx.auth;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.common.constant.Constants;
import com.chat.server.service.auth.AuthService;
import com.chat.server.service.auth.request.LoginRequest;
import com.chat.server.service.auth.request.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Auth")
@Controller
@RequestMapping("/hx/auth")
@RequiredArgsConstructor
public class AuthHxController {
    private final AuthService authService;

    @Operation(summary = "회원가입 모달")
    @GetMapping("/signup/modal")
    public List<ModelAndView> signupModal() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/auth/signup.html",
                        "components/auth/signup :: signup")
                .build();
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public List<ModelAndView> login(@ModelAttribute @Valid SignupRequest request) {
        authService.createUser(request);
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "signup success"))
                .build();
    }

    @Operation(summary = "로그인 모달")
    @GetMapping("/login/modal")
    public List<ModelAndView> loginModal() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/auth/login.html",
                        "components/auth/login :: login")
                .build();
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public List<ModelAndView> login(@ModelAttribute @Valid LoginRequest request,
                                    HttpServletResponse response) {
        authService.login(request, response);
        response.setHeader(Constants.HX_RELOAD, "true");
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "login success"))
                .build();
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public List<ModelAndView> logout(HttpServletResponse response) {
        authService.logout(response);
        response.setHeader(Constants.HX_RELOAD, "true");
        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/toast.html",
                        "components/common/toast :: message",
                        Map.of("type", "success", "message", "logout success"))
                .build();
    }
}
