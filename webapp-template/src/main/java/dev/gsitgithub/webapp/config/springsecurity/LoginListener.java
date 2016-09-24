package dev.gsitgithub.webapp.config.springsecurity;

import dev.gsitgithub.webapp.config.logging.MDC;
import dev.gsitgithub.webapp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Period;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.web.authentication.switchuser.AuthenticationSwitchUserEvent;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static dev.gsitgithub.webapp.config.utils.ApplicationUtils.*;
import static dev.gsitgithub.webapp.config.utils.StringUtils.quote;
import static dev.gsitgithub.webapp.config.utils.TimeUtils.format;

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

    private void setAutomaticLogoutTime(Period automaticLogoutTime) {
        log.info("Setting automatic logout time to {}", format(automaticLogoutTime));
        int automaticLogoutTimeInSeconds = automaticLogoutTime.toStandardSeconds().getSeconds();
        getSession().setMaxInactiveInterval(automaticLogoutTimeInSeconds);
    }

}
