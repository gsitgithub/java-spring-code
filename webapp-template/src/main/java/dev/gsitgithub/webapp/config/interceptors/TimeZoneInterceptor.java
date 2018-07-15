package dev.gsitgithub.webapp.config.interceptors;

import dev.gsitgithub.webapp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.datetime.joda.JodaTimeContext;
import org.springframework.format.datetime.joda.JodaTimeContextHolder;
import org.springframework.format.datetime.standard.DateTimeContext;
import org.springframework.format.datetime.standard.DateTimeContextHolder;

import static dev.gsitgithub.webapp.config.utils.StringUtils.quote;

import java.time.ZoneId;

@Slf4j
public class TimeZoneInterceptor extends ParameterInterceptor<ZoneId> {

    public TimeZoneInterceptor(String parameterName) {
        super(parameterName);
    }

    @Override
    protected String printParameter(ZoneId timeZone) {
        return timeZone.getId();
    }

    @Override
    protected ZoneId parseParameter(String timeZone) {
        try {
            return ZoneId.of(timeZone);
        } catch (IllegalArgumentException e) {
            log.warn("Setting timezone to = " + quote(timeZone) + " by parameter failed, the timezone does not exist. Please use one of the following time zones: " + ZoneId.getAvailableZoneIds());
            return null;
        }
    }

    @Override
    protected ZoneId getParameterFromUserSettings(User user) {
        return user.getTimeZone();
    }

    @Override
    protected void setParameterHolder(ZoneId timeZone) {
        DateTimeContext context = new DateTimeContext();
        context.setTimeZone(timeZone);
        DateTimeContextHolder.setDateTimeContext(context);
    }

    @Override
    protected void resetParameterHolder() {
        JodaTimeContextHolder.resetJodaTimeContext();
    }

    @Override
    protected ZoneId getDefaultValue() {
        return ZoneId.systemDefault();
    }
}
