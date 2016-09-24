package dev.gsitgithub.webapp.config.interceptors;

import dev.gsitgithub.webapp.entity.User;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.springframework.util.StringUtils.parseLocaleString;

public class LocaleInterceptor extends ParameterInterceptor<Locale> {

    public LocaleInterceptor(String parameterName) {
        super(parameterName);
    }

    @Override
    protected String printParameter(Locale locale) {
        return locale.toString();
    }

    @Override
    protected Locale parseParameter(String locale) {
        return parseLocaleString(locale);
    }

    @Override
    protected Locale getParameterFromUserSettings(User user) {
        return user.getLocale();
    }

    @Override
    protected void setParameterHolder(Locale locale) {
        LocaleContextHolder.setLocale(locale, true);
    }

    @Override
    protected void resetParameterHolder() {
        LocaleContextHolder.resetLocaleContext();
    }

    @Override
    protected Locale getDefaultValue() {
        return Locale.getDefault();
    }
}
