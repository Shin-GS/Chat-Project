package com.chat.server.controller.api;

import com.chat.server.common.Response;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.security.JwtMember;
import com.chat.server.security.JwtMemberInfo;
import com.chat.server.service.AuthService;
import com.chat.server.service.request.CreateUserRequest;
import com.chat.server.service.request.LoginRequest;
import com.chat.server.service.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public Response<Object> createUser(@RequestBody @Valid CreateUserRequest request) {
        authService.createUser(request);
        return Response.of(SuccessCode.USER_CREATED);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return Response.of(SuccessCode.USER_LOGGED_IN, authService.login(request));
    }

    @Operation(summary = "사용자 번호 조회",
            security = @SecurityRequirement(name = Constants.SWAGGER_ACCESS_TOKEN))
    @GetMapping("/me/id")
    public Response<Long> getUserId(@JwtMember JwtMemberInfo memberInfo) {
        return Response.of(SuccessCode.USER_INFO_RETRIEVED, memberInfo.id());
    }

    @Operation(summary = "사용자 이름 조회",
            security = @SecurityRequirement(name = Constants.SWAGGER_ACCESS_TOKEN))
    @GetMapping("/me/name")
    public Response<String> getUserName(@JwtMember JwtMemberInfo memberInfo) {
        return Response.of(SuccessCode.USER_INFO_RETRIEVED, memberInfo.username());
    }
}
