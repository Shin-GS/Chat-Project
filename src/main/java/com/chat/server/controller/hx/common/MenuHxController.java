package com.chat.server.controller.hx.common;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.service.security.JwtMember;
import com.chat.server.service.security.JwtMemberInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Tag(name = "Menu")
@Controller
@RequestMapping("/hx/menus")
@RequiredArgsConstructor
public class MenuHxController {
    @Operation(summary = "메뉴")
    @GetMapping
    public List<ModelAndView> menu(@JwtMember JwtMemberInfo memberInfo) {
        if (ObjectUtils.isEmpty(memberInfo)) {
            return new ModelAndViewBuilder()
                    .addFragment("templates/components/common/menu.html",
                            "components/common/menu :: guest-menu")
                    .build();
        }

        return new ModelAndViewBuilder()
                .addFragment("templates/components/common/menu.html",
                        "components/common/menu :: user-menu",
                        Map.of("member", memberInfo))
                .build();
    }
}
