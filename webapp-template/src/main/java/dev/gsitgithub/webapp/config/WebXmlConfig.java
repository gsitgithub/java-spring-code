package dev.gsitgithub.webapp.config;

import dev.gsitgithub.webapp.config.logging.MDCInsertingServletFilter;
import lombok.extern.slf4j.Slf4j;
import org.h2.server.web.WebServlet;
import org.jminix.console.servlet.MiniConsoleServlet;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.IOException;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.join;

@Slf4j
public class WebXmlConfig implements WebApplicationInitializer {
    
    final String TARGET_FOLDER = getClass().getClassLoader().getResource(".").getPath().replaceAll("/classes/$", "");
    public final static String PROFILE_ALL = "all";
    public final static String PROFILE_SECURITY = "security";
    public final static String PROFILE_DB = "db";
    public final static String PROFILE_METRICS = "metrics";
    public final static String PROFILE_WEB = "web";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	
        // Load Application Properties
        String applicationEnvironment = getApplicationEnvironment();
        Properties applicationProperties = loadApplicationProperties(applicationEnvironment);
        // Set init parameter to activate provided profiles
        setInitParameters(servletContext, applicationProperties);
        
        // Set Java Melody settings
        servletContext.setInitParameter("javamelody.monitoring-path", "/javamelody");
        servletContext.setInitParameter("javamelody.storage-directory", TARGET_FOLDER + "/logs/javamelody");
        servletContext.setInitParameter("javamelody.resolution-seconds", "60");
        servletContext.setInitParameter("javamelody.disabled", "false");

        // Create Application Context
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(ApplicationConfig.class);
        appContext.setDisplayName(applicationProperties.getProperty("application.name"));
        appContext.getEnvironment().setActiveProfiles(applicationEnvironment, PROFILE_SECURITY);
        log.info("Starting up Application with the following active profiles: " + join(appContext.getEnvironment().getActiveProfiles(), ", "));

        // Enable Application Context with Context Loader Listner
        servletContext.addListener(new ContextLoaderListener(appContext));

        // Enable SessionDestroyedEvent for the LogOutListener
        servletContext.addListener(new HttpSessionEventPublisher());

        // Make RequestContextHolder available in filters
        servletContext.addListener(new RequestContextListener());

        // Register Web Dispatcher Servlet
        ServletRegistration.Dynamic dispatcherServlet =
                servletContext.addServlet("dispatcherServlet", new DispatcherServlet(appContext));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/");

        // Register Rest Dispatcher Servlet
        /*
        AnnotationConfigWebApplicationContext restContext = new AnnotationConfigWebApplicationContext();
        restContext.register(RestServletContextConfiguration.class);
        DispatcherServlet servlet = new DispatcherServlet(restContext);
        servlet.setDispatchOptionsRequest(true);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "springRestDispatcher", servlet
        );
        dispatcher.setLoadOnStartup(2);
        dispatcher.addMapping("/rapi*//*");
        */

        // Add data to Logback MDC
        FilterRegistration.Dynamic mdcInsertingServletFilter =
                servletContext.addFilter("mdcInsertingServletFilter", MDCInsertingServletFilter.class);
        mdcInsertingServletFilter.addMappingForUrlPatterns(null, false, "/*");

        // Register Spring Security Filter
        FilterRegistration.Dynamic springSecurityFilterChain =
                servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
        springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");


        // Register UTF-8 Encoding Filter, see http://developers-blog.org/blog/default/2010/08/17/Spring-MVC-and-UTF-8-Encoding-with-CharacterEncodingFilter
        FilterRegistration.Dynamic encodingFilter =
                servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(null, false, "/*");

        // Register REST GET, PUT, POST, and DELETE Support Filter, see http://static.springsource.org/spring/docs/current/spring-framework-reference/html/view.html#rest-method-conversion
        FilterRegistration.Dynamic httpMethodFilter =
                servletContext.addFilter("httpMethodFilter", HiddenHttpMethodFilter.class);
        httpMethodFilter.addMappingForUrlPatterns(null, false, "/*");

        // Register jMiniX Console Servlet
        ServletRegistration.Dynamic jminixConsoleServlet =
                servletContext.addServlet("jMiniXConsoleServlet", new MiniConsoleServlet());
        jminixConsoleServlet.addMapping("/admin/jminix/*");


        // Register Database Manager Web Servlet
        ServletRegistration.Dynamic databaseManagerWebServlet =
                servletContext.addServlet("databaseManagerWebServlet", new WebServlet());
        databaseManagerWebServlet.setInitParameter("webAllowOthers", "");
        databaseManagerWebServlet.setLoadOnStartup(1);
        databaseManagerWebServlet.addMapping("/database/*");

        if (!"production".equalsIgnoreCase(applicationEnvironment)) {
            // If logback configured
            // Enable Logback Access Request-Response
            enableHttpLogbackAccess(servletContext);
            // Providing URL for only Logback Status with OnStatusConsoleListener
            enableHttpLogbackStatus(servletContext);
        }
    }

    private String getApplicationEnvironment() {
        String applicationEnvironment = System.getProperty("application.environment");
        if (isBlank(applicationEnvironment)) {
            // No application environment provided, use localhost as default
            log.info("No application.environment set in System Properties, setting default application.environment = localhost");
            System.setProperty("application.environment", "localhost");
            return "localhost";
        }
        return applicationEnvironment;
    }

    private Properties loadApplicationProperties(String applicationEnvironment) {
        Properties properties = new Properties();
        String applicationPropertiesPath = "application_" + applicationEnvironment + ".properties";
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(applicationPropertiesPath));
        } catch (NullPointerException ex) {
            throw new RuntimeException("Did not find application_" + applicationEnvironment + ".properties file in src/main/resources folder for application.environment = " + applicationEnvironment, ex);
        } catch (IOException ex) {
            throw new RuntimeException("Could not read application_" + applicationEnvironment + ".properties file in src/main/resources folder", ex);
        }
        return properties;
    }
    
    private void setInitParameters(ServletContext sc, Properties applicationProperties) {
    	String key = "spring.profiles.active";
    	if (applicationProperties.containsKey(key))
    		sc.setInitParameter(key, applicationProperties.getProperty(key));
    }

    private void enableHttpLogbackAccess(ServletContext servletContext){
        // Enable Logback Access Request-Response, not supported in Log4J
        //FilterRegistration.Dynamic teeFilter = servletContext.addFilter("teeFilter", TeeFilter.class);
        //teeFilter.addMappingForUrlPatterns(null, false, "/*");
    }

    private void enableHttpLogbackStatus(ServletContext servletContext){
        // Providing URL for Logback Status with OnStatusConsoleListener, not supported in Log4J
        //ServletRegistration.Dynamic viewStatusMessages = servletContext.addServlet("viewStatusMessages", new ViewStatusMessagesServlet());
        //viewStatusMessages.addMapping("/admin/logback");
    }
}
