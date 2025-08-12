package com.chat.server.config.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.*;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingTimeProvider")
public class JpaAuditConfig {
    @Bean
    public DateTimeProvider auditingTimeProvider(JpaAuditProps p) {
        ZoneId zone = ZoneId.of(p.getZone());
        if (p.isInstant()) {
            Clock clock = Clock.system(zone);
            return () -> Optional.of(Instant.now(clock));
        }

        if (p.isOffset()) {
            return () -> Optional.of(OffsetDateTime.now(zone));
        }

        return () -> Optional.of(LocalDateTime.now(zone).withNano(0));
    }
}
