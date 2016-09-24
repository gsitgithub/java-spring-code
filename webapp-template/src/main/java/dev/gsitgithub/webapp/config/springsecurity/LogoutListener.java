package dev.gsitgithub.webapp.config.springsecurity;

import dev.gsitgithub.webapp.config.logging.MDC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import static dev.gsitgithub.webapp.config.utils.StringUtils.quote;

@Slf4j
@Component
public class LogoutListener implements ApplicationListener<SessionDestroyedEvent> {

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        String sessionId = (String) ((HttpSessionDestroyedEvent) event).getSession().getId();
        String username = (String) ((HttpSessionDestroyedEvent) event).getSession().getAttribute("username");
        MDC.putSession(sessionId);
        MDC.putUsername(username);
        log.info("Logged out as {}", quote(username));
        MDC.removeSession();
        MDC.removeUsername();
    }
}
