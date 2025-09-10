package com.chat.server.config.locale;

import com.chat.server.common.constant.Constants;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.time.Duration;
import java.util.Locale;

@Configuration
public class LocaleConfig {
    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasenames("messages");
        ms.setDefaultEncoding("UTF-8");
        ms.setFallbackToSystemLocale(false);
        ms.setUseCodeAsDefaultMessage(true);
        return ms;
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver(Constants.COOKIE_LOCALE_NAME);
        resolver.setDefaultLocale(Locale.ENGLISH);
        resolver.setCookiePath("/");
        resolver.setCookieMaxAge(Duration.ofDays(365));
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        ResettingLocaleChangeInterceptor localeChangeInterceptor = new ResettingLocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(Constants.COOKIE_LOCALE_NAME); // ?lang=ko / ?lang=ko-KR
        return localeChangeInterceptor;
    }
}
