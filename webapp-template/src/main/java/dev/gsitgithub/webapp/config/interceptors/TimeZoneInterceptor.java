package dev.gsitgithub.webapp.config.interceptors;

import dev.gsitgithub.webapp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeZone;
import org.springframework.format.datetime.joda.JodaTimeContext;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;

import static dev.gsitgithub.webapp.config.utils.StringUtils.quote;

@Slf4j
public class TimeZoneInterceptor extends ParameterInterceptor<DateTimeZone> {

    public TimeZoneInterceptor(String parameterName) {
        super(parameterName);
    }

    @Override
    protected String printParameter(DateTimeZone timeZone) {
        return timeZone.getID();
    }

    @Override
    protected DateTimeZone parseParameter(String timeZone) {
        try {
            return DateTimeZone.forID(timeZone);
        } catch (IllegalArgumentException e) {
            log.warn("Setting timezone to = " + quote(timeZone) + " by parameter failed, the timezone does not exist. Please use one of the following time zones: " + DateTimeZone.getAvailableIDs());
            return null;
        }
    }

    @Override
    protected DateTimeZone getParameterFromUserSettings(User user) {
        return user.getTimeZone();
    }

    @Override
    protected void setParameterHolder(DateTimeZone timeZone) {
        JodaTimeContext context = new JodaTimeContext();
        context.setTimeZone(timeZone);
        JodaTimeContextHolder.setJodaTimeContext(context);
    }

    @Override
    protected void resetParameterHolder() {
        JodaTimeContextHolder.resetJodaTimeContext();
    }

    @Override
    protected DateTimeZone getDefaultValue() {
        return DateTimeZone.getDefault();
    }
}
