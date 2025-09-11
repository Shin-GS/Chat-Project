package com.chat.server.controller.hx.common;

import com.chat.server.common.constant.Constants;
import com.chat.server.common.response.ModelAndViewBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Tag(name = "Common")
@Controller
@Slf4j
public class LocaleHxController {
    @Operation(summary = "언어 변경")
    @PostMapping("/hx/common/locale")
    public List<ModelAndView> changeLocale(@RequestParam("lang") String lang,
                                           HttpServletResponse response) {
        // LocaleChangeInterceptor already intercepted this request
        // and updated the user's Locale in the configured LocaleResolver
        // Just send HTMX reload header so the page is re-rendered
        log.info("Language change request received: lang={}", lang);
        response.setHeader(Constants.HX_RELOAD, "true");
        return new ModelAndViewBuilder()
                .build();
    }
}
