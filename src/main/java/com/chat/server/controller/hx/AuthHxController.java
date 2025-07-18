package com.chat.server.controller.hx;

import com.chat.server.common.template.ModelAndViewBuilder;
import com.chat.server.service.AuthService;
import com.chat.server.service.request.LoginRequest;
import com.chat.server.service.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hx/auth")
@RequiredArgsConstructor
public class AuthHxController {
    private final AuthService authService;

    @GetMapping("/login")
    public List<ModelAndView> login() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/auth/login.html",
                        "components/auth/login :: login")
                .build();
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public List<ModelAndView> login(@ModelAttribute @Valid LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", loginResponse.token())
                .httpOnly(true)   // JS 접근 차단
                .secure(true)     // HTTPS일 경우 필수
                .path("/")
                .maxAge(Duration.ofHours(1))
                .sameSite("Lax")
                .build();

        return new ModelAndViewBuilder()
                .addFragment("templates/components/toast.html",
                        "components/toast :: message",
                        Map.of("type", "success", "message", "login success"))
                .build();
    }
}
