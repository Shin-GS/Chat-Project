package com.chat.server.controller.api;

import com.chat.server.common.Response;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.model.request.CreateUserRequest;
import com.chat.server.model.request.LoginRequest;
import com.chat.server.model.response.LoginResponse;
import com.chat.server.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApi {
    private final AuthService authService;

    @PostMapping("/create-user")
    public Response<Object> createUser(@RequestBody @Valid CreateUserRequest request) {
        authService.createUser(request);
        return Response.of(SuccessCode.Success);
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return Response.of(SuccessCode.Success, authService.login(request));
    }

    // todo 이후에 header 에 토큰 setting 하게 변경 필요
    @GetMapping("/verified-status/id/{token}")
    public Response<Long> getUserId(@PathVariable("token") String token) {
        return Response.of(SuccessCode.Success, authService.getUserIdFromToken(token));
    }

    // todo 이후에 header 에 토큰 setting 하게 변경 필요
    @GetMapping("/verified-status/name/{token}")
    public Response<String> getUserName(@PathVariable("token") String token) {
        return Response.of(SuccessCode.Success, authService.getUsernameFromToken(token));
    }
}
