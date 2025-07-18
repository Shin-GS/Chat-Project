package com.chat.server.controller.hx;

import com.chat.server.common.ModelAndViewBuilder;
import com.chat.server.security.JwtMember;
import com.chat.server.security.JwtMemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hx")
@RequiredArgsConstructor
public class MenuHxController {
    @GetMapping("/menu")
    public List<ModelAndView> menu(@JwtMember JwtMemberInfo memberInfo) {
        if (ObjectUtils.isEmpty(memberInfo)) {
            return new ModelAndViewBuilder()
                    .addFragment("templates/components/menu.html",
                            "components/menu :: guest-menu")
                    .build();
        }

        return new ModelAndViewBuilder()
                .addFragment("templates/components/menu.html",
                        "components/menu :: user-menu",
                        Map.of("member", memberInfo))
                .build();
    }
}
