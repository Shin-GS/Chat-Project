package com.chat.server.config;

import com.chat.server.common.constant.Constants;
import com.chat.server.common.exception.CustomTokenException;
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

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenResolver.resolveAccessToken();
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
            if ("true".equals(request.getHeader("HX-Request"))) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader(Constants.HEADER_AUTHORIZATION_INVALID, "true");
                response.setContentType("text/html;charset=UTF-8");
                String toastHtml = """
                        <div id="toast-container" hx-swap-oob="true">
                            <div id="toast-error"
                                 class="fixed bottom-5 left-1/2 transform -translate-x-1/2 px-4 py-3 rounded shadow-lg 
                                        transition-opacity duration-300 text-white bg-red-500">
                                <p>%s</p>
                            </div>
                        </div>
                        """.formatted(e.getMessage());
                response.getWriter().write(toastHtml);

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            }
        }
    }
}
