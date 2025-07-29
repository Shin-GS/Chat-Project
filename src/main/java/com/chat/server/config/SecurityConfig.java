package com.chat.server.config;

import com.chat.server.filter.JwtAuthenticationFilter;
import com.chat.server.filter.UnauthorizedEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final RestrictedPaths restrictedPaths;
    private final UnauthorizedEntryPoint unauthorizedEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(toArray(restrictedPaths.getStaticPath())).authenticated()
                        .requestMatchers(toArray(restrictedPaths.getApiPath())).authenticated()
                        .requestMatchers(toArray(restrictedPaths.getHxApiPath())).authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedEntryPoint))
                .build();
    }

    private String[] toArray(List<String> paths) {
        return paths == null ? new String[0] : paths.toArray(new String[0]);
    }
}
