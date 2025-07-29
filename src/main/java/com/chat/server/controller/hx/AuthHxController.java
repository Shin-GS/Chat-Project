package com.chat.server.controller.hx;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.common.constant.Constants;
import com.chat.server.service.AuthService;
import com.chat.server.service.request.LoginRequest;
import com.chat.server.service.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    public List<ModelAndView> login(@ModelAttribute @Valid LoginRequest request,
                                    HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);
        response.setHeader(Constants.HEADER_AUTHORIZATION, "Bearer " + loginResponse.token());
        response.setHeader(Constants.HEADER_AUTHORIZATION_REFRESH, "Bearer " + loginResponse.refreshToken());

        return new ModelAndViewBuilder()
                .addFragment("templates/components/toast.html",
                        "components/toast :: message",
                        Map.of("type", "success", "message", "login success"))
                .addFragment("templates/components/menu.html",
                        "components/menu :: user-menu",
                        Map.of("member", authService.getMemberInfo(loginResponse.token())))
                .build();
    }
}
