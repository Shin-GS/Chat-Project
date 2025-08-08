package com.chat.server.filter;

import com.chat.server.common.exception.CustomTokenException;
import com.chat.server.common.util.ResponseUtil;
import com.chat.server.security.JwtMemberInfo;
import com.chat.server.security.TokenResolver;
import com.chat.server.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenResolver tokenResolver;
    private final AuthService authService;
    private final ResponseUtil responseUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenResolver.resolveAccessToken(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            JwtMemberInfo memberInfo = authService.getMemberInfo(token);
            if (memberInfo != null) {
                String roleName = "ROLE_" + memberInfo.role().name();
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberInfo, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (CustomTokenException e) {
            responseUtil.unAuthorizationResponse(request, response, e);
        }
    }
}
