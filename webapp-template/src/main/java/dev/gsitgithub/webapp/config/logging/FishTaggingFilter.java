package dev.gsitgithub.webapp.config.logging;

import org.apache.logging.log4j.ThreadContext;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by gsitgithub on 10/5/2015.
 */
@WebFilter(urlPatterns = "/*", dispatcherTypes = {
        DispatcherType.REQUEST,
        DispatcherType.ERROR,
        DispatcherType.FORWARD,
        DispatcherType.INCLUDE,
        DispatcherType.ASYNC
})
public class FishTaggingFilter implements Filter{
    private final String ID = "id";
    private final String USERNAME = "username";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean clear = false;
        if (!ThreadContext.containsKey(ID)) {
            clear = true;
            ThreadContext.put(ID, UUID.randomUUID().toString());

            /* Only if app uses session management. */

            HttpSession session = ((HttpServletRequest)request).getSession(false);
            if (session != null)
                ThreadContext.push(USERNAME, (String)session.getAttribute(USERNAME));
        }
        try {
            chain.doFilter(request,response);
        } finally {
            if (clear)
                ThreadContext.clearAll();
        }
    }

    @Override
    public void destroy() {

    }
}
