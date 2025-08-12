package com.chat.server.controller.api;

import com.chat.server.common.Response;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import com.chat.server.service.AuthService;
import com.chat.server.service.request.SignupRequest;
import com.chat.server.service.request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {
    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/sign-in")
    public Response<Object> createUser(@RequestBody @Valid SignupRequest request) {
        authService.createUser(request);
        return Response.of(SuccessCode.USER_CREATED);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public Response<Object> login(@RequestBody @Valid LoginRequest request,
                                  HttpServletResponse response) {
        authService.login(request, response);
        return Response.of(SuccessCode.USER_LOGGED_IN);
    }

    @Operation(summary = "사용자 번호 조회")
    @GetMapping("/me/id")
    public Response<Long> getUserId(@JwtMember JwtMemberInfo memberInfo) {
        return Response.of(SuccessCode.USER_INFO_RETRIEVED, memberInfo.id());
    }

    @Operation(summary = "사용자 이름 조회")
    @GetMapping("/me/name")
    public Response<String> getUserName(@JwtMember JwtMemberInfo memberInfo) {
        return Response.of(SuccessCode.USER_INFO_RETRIEVED, memberInfo.username());
    }
}
