package com.chat.server.filter.socket;

import com.chat.server.service.security.JwtMemberInfo;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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

        if (accessor.getSessionAttributes().get("memberInfo") instanceof JwtMemberInfo memberInfo) {
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + memberInfo.role().name()));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(memberInfo.id(), null, authorities);
            auth.setDetails(memberInfo);
            accessor.setUser(auth);
        }

        return message;
    }
}
