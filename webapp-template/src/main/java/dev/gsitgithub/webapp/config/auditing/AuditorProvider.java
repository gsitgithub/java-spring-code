package dev.gsitgithub.webapp.config.auditing;

import static dev.gsitgithub.webapp.config.utils.ApplicationUtils.getUser;

import java.util.Optional;

import dev.gsitgithub.webapp.entity.User;
import org.springframework.data.domain.AuditorAware;


public class AuditorProvider implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.of( getUser() );
    }
}
