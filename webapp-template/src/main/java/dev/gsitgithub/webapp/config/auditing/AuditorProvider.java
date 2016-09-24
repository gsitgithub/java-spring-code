package dev.gsitgithub.webapp.config.auditing;

import static dev.gsitgithub.webapp.config.utils.ApplicationUtils.getUser;
import dev.gsitgithub.webapp.entity.User;
import org.springframework.data.domain.AuditorAware;


public class AuditorProvider implements AuditorAware<User> {

    @Override
    public User getCurrentAuditor() {
        return getUser();
    }
}
