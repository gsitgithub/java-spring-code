package dev.gsitgithub.webapp.config.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static dev.gsitgithub.webapp.config.utils.ApplicationUtils.getUsername;

@Slf4j
public class MDCUsernameInsertingFilter extends GenericFilterBean implements InitializingBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        MDC.putUsername(getUsername());
        chain.doFilter(request, response);
    }

}
