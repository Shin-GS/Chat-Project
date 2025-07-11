package com.chat.server.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "token")
@Getter
@Setter
public class JwtProperties {
    private String secretKey;
    private String refreshSecretKey;
    private long tokenTime;
    private long refreshTokenTime;
}
