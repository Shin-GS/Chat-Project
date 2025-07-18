package com.chat.server.common.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TemplateRenderer {
    private final SpringTemplateEngine templateEngine;

    public TemplateRenderer(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String render(String templateName, Map<String, Object> model) {
        Context context = new Context();
        context.setVariables(model);
        return templateEngine.process(templateName, context);
    }

    public String render(List<ModelAndView> views) {
        try {
            return views.stream()
                    .map(view -> render(view.getViewName(), view.getModel()))
                    .collect(Collectors.joining("\n"));

        } catch (Exception ex) {
            log.error("🔥 Thymeleaf 렌더링 실패", ex);
            throw ex;
        }
    }
}
