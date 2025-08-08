package com.chat.server.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.restricted-paths")
public class RestrictedPaths {
    private List<String> staticPath;
    private List<String> apiPath;
    private List<String> hxApiPath;
}
