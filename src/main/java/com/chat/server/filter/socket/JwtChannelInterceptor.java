package com.chat.server.filter.socket;

import com.chat.server.service.security.JwtMemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(@NonNull Message<?> message,
                              @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null
                || !StompCommand.CONNECT.equals(accessor.getCommand())
                || accessor.getSessionAttributes() == null
                || !accessor.getSessionAttributes().containsKey("memberInfo")) {
            return message;
        }

        Object memberInfoAttr = accessor.getSessionAttributes().get("memberInfo");
        if (memberInfoAttr instanceof JwtMemberInfo memberInfo) {
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + memberInfo.role().name()));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(memberInfo, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        return message;
    }
}
