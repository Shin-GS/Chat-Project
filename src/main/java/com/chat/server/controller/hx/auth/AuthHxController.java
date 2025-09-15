package com.chat.server.controller.hx.auth;

import com.chat.server.common.code.CodeMessageGetter;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.common.response.ModelAndViewBuilder;
import com.chat.server.service.auth.AuthService;
import com.chat.server.service.auth.request.LoginRequest;
import com.chat.server.service.auth.request.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import static com.chat.server.common.constant.FragmentConstants.*;

@Tag(name = "Auth")
@Controller
@RequestMapping("/hx/auth")
@RequiredArgsConstructor
public class AuthHxController {
    private final AuthService authService;
    private final CodeMessageGetter codeMessageGetter;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public List<ModelAndView> login(@ModelAttribute @Valid SignupRequest request,
                                    HttpServletResponse response) {
        authService.createUser(request, response);
        response.setHeader(Constants.HX_REDIRECT, "/");
        return new ModelAndViewBuilder()
                .addFragment(COMMON_TOAST_PATH,
                        COMMON_TOAST_MESSAGE_FRAGMENT,
                        Map.of(COMMON_TOAST_TYPE, COMMON_TOAST_TYPE_SUCCESS,
                                COMMON_TOAST_MESSAGE, codeMessageGetter.getMessage(SuccessCode.USER_CREATED)))
                .build();
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public List<ModelAndView> login(@ModelAttribute @Valid LoginRequest request,
                                    HttpServletResponse response) {
        authService.login(request, response);
        response.setHeader(Constants.HX_REDIRECT, "/");
        return new ModelAndViewBuilder()
                .addFragment(COMMON_TOAST_PATH,
                        COMMON_TOAST_MESSAGE_FRAGMENT,
                        Map.of(COMMON_TOAST_TYPE, COMMON_TOAST_TYPE_SUCCESS,
                                COMMON_TOAST_MESSAGE, codeMessageGetter.getMessage(SuccessCode.USER_LOGGED_IN)))
                .build();
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public List<ModelAndView> logout(HttpServletResponse response) {
        authService.logout(response);
        response.setHeader(Constants.HX_RELOAD, "true");
        return new ModelAndViewBuilder()
                .addFragment(COMMON_TOAST_PATH,
                        COMMON_TOAST_MESSAGE_FRAGMENT,
                        Map.of(COMMON_TOAST_TYPE, COMMON_TOAST_TYPE_SUCCESS,
                                COMMON_TOAST_MESSAGE, codeMessageGetter.getMessage(SuccessCode.USER_LOGGED_OUT)))
                .build();
    }
}
