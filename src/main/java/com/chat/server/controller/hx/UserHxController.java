package com.chat.server.controller.hx;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.security.JwtMember;
import com.chat.server.security.JwtMemberInfo;
import com.chat.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hx/users")
@RequiredArgsConstructor
public class UserHxController {
    private final UserService userService;

    @Operation(summary = "본인 제외한 비슷한 이름 조회 모달")
    @GetMapping("/find/names/modal")
    public List<ModelAndView> findSimilarUsernamesModal() {
        return new ModelAndViewBuilder()
                .addFragment("templates/components/chat/friend/searchModal.html",
                        "components/chat/friend/searchModal :: search-modal")
                .build();
    }

    @Operation(summary = "본인 제외한 비슷한 이름 조회")
    @GetMapping("/find/names")
    public List<ModelAndView> findSimilarUsernames(@RequestParam("keyword") String keyword,
                                                   @JwtMember JwtMemberInfo memberInfo) {
        List<String> searchNames = userService.findSimilarNamesExcludingExactMatch(keyword, memberInfo.username());
        return new ModelAndViewBuilder()
                .addFragment("templates/components/chat/friend/friendList.html",
                        "components/chat/friend/friendList :: friend-list",
                        Map.of("names", searchNames))
                .build();
    }
}
