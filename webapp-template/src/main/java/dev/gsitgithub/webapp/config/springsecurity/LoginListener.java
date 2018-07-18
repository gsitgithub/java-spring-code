package dev.gsitgithub.webapp.config.springsecurity;

import static dev.gsitgithub.webapp.config.utils.ApplicationUtils.getSession;
import static dev.gsitgithub.webapp.config.utils.ApplicationUtils.getUser;
import static dev.gsitgithub.webapp.config.utils.ApplicationUtils.getUsername;
import static dev.gsitgithub.webapp.config.utils.StringUtils.quote;
import static dev.gsitgithub.webapp.config.utils.TimeUtils.format;

import java.time.Duration;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.web.authentication.switchuser.AuthenticationSwitchUserEvent;
import org.springframework.stereotype.Component;

import dev.gsitgithub.webapp.config.logging.MDC;
import dev.gsitgithub.webapp.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginListener {

    @Component
    static class FormLoginAndRememberMeLoginListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

        @Inject
        LoginListener listener;

        @Override
        public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
            MDC.putSession(getSession().getId());
            MDC.putUsername(getUsername());
            getSession().setAttribute("username", getUsername());
            log.info("Logged in as {}", quote(getUsername()));
            listener.onLogin(getUser());
        }
    }

    @Component
    static class SwitchUserLoginListener implements ApplicationListener<AuthenticationSwitchUserEvent> {

        @Inject
        LoginListener listener;

        @Override
        public void onApplicationEvent(AuthenticationSwitchUserEvent event) {
            User fromUser = (User) event.getAuthentication().getPrincipal();
            User toUser = (User) event.getTargetUser();

            log.info("Logged out as {} (switching to {})", quote(fromUser.getUsername()), quote(toUser.getUsername()));
            MDC.putSession(getSession().getId());
            MDC.putUsername(toUser.getUsername());
            getSession().setAttribute("username", toUser.getUsername());
            log.info("Logged in as {} (switched from {})", quote(toUser.getUsername()), quote(fromUser.getUsername()));
            listener.onLogin(toUser);
        }
    }

    private void onLogin(User user) {
        setAutomaticLogoutTime(user.getAutomaticLogoutTime());
    }

    private void setAutomaticLogoutTime(Duration automaticLogoutTime) {
    	int automaticLogoutTimeInSeconds = (int) automaticLogoutTime.toMillis();
        log.info("Setting automatic logout time to {}", format(automaticLogoutTimeInSeconds));
        getSession().setMaxInactiveInterval(automaticLogoutTimeInSeconds);
    }

}
