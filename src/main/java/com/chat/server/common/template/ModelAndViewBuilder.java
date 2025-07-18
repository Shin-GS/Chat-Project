package com.chat.server.common.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ModelAndViewBuilder {
    private final List<ModelAndView> fragments = new ArrayList<>();

    public ModelAndViewBuilder addFragment(String developOnlyPath,
                                           String viewName) {
        log.debug(developOnlyPath);
        this.fragments.add(new ModelAndView(viewName, Map.of()));
        return this;
    }

    public ModelAndViewBuilder addFragment(String developOnlyPath,
                                           String viewName,
                                           Map<String, Object> model) {
        log.debug(developOnlyPath);
        this.fragments.add(new ModelAndView(viewName, model));
        return this;
    }

    public ModelAndViewBuilder addFragment(String developOnlyPath,
                                           String viewName,
                                           String key,
                                           String value) {
        log.debug(developOnlyPath);
        this.fragments.add(new ModelAndView(viewName, Map.of(key, value)));
        return this;
    }

    public List<ModelAndView> build() {
        return fragments;
    }
}
