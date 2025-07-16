package com.chat.server.controller.api;

import com.chat.server.common.Response;
import com.chat.server.common.code.SuccessCode;
import com.chat.server.common.constant.Constants;
import com.chat.server.security.JwtMember;
import com.chat.server.security.JwtMemberInfo;
import com.chat.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserApi {
    private final UserService userService;

    @Operation(summary = "비슷한 이름 조회",
            security = @SecurityRequirement(name = Constants.SWAGGER_ACCESS_TOKEN))
    @GetMapping("/find/names/{keyword}")
    public Response<List<String>> findSimilarUsernames(@PathVariable("keyword") String keyword,
                                                       @JwtMember JwtMemberInfo memberInfo) {
        return Response.of(SuccessCode.USER_INFO_RETRIEVED, userService.findSimilarNamesExcludingExactMatch(keyword, memberInfo.username()));
    }
}
