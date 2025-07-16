package com.chat.server.controller.hx;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hx")
@RequiredArgsConstructor
public class MenuHxController {
//    private final RefreshInfoFetcher refreshInfoFetcher;
//
//    @GetMapping("/menu")
//    public List<ModelAndView> menu(@SessionMemberId Long memberId) {
//        if (ObjectUtils.isEmpty(memberId)) {SessionMember
//            return new ModelAndViewBuilder()
//                    .addFragment("templates/components/menu.html",
//                            "components/menu :: guest-menu")
//                    .build();
//        }
//
//        RefreshInfoResponse refresh = refreshInfoFetcher.refresh(memberId);
//        return new ModelAndViewBuilder()
//                .addFragment("templates/components/menu.html",
//                        "components/menu :: user-menu",
//                        Map.of("info", refresh))
//                .build();
//    }
}
