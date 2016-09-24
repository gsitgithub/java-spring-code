package dev.gsitgithub.webapp.config.auditing;

import dev.gsitgithub.webapp.config.auditing.AuditorProvider;
import dev.gsitgithub.webapp.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

    @Bean
    public AuditorAware<User> auditorProvider() {
        return new AuditorProvider();
    }
}
