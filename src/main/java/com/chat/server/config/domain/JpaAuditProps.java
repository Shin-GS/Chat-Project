package com.chat.server.config.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.audit")
public class JpaAuditProps {
    private String zone = "UTC";
    private String temporalType = "instant";

    public boolean isInstant() {
        return Objects.equals(temporalType, "instant");
    }

    public boolean isOffset() {
        return Objects.equals(temporalType, "offset");
    }

    public boolean isLocal() {
        return Objects.equals(temporalType, "local");
    }
}
