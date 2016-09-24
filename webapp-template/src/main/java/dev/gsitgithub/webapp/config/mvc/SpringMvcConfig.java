package dev.gsitgithub.webapp.config.mvc;

import dev.gsitgithub.webapp.config.format.custom.CustomFormatAnnotationFormatterFactory;
import dev.gsitgithub.webapp.config.format.json.JsonFormatAnnotationFormatterFactory;
import dev.gsitgithub.webapp.config.format.list.ListFormatAnnotationFormatterFactory;
import dev.gsitgithub.webapp.config.interceptors.LocaleInterceptor;
import dev.gsitgithub.webapp.config.interceptors.TimeZoneInterceptor;
import dev.gsitgithub.webapp.config.logging.ExecutionTimeInterceptor;
import dev.gsitgithub.webapp.config.utils.ApplicationEnvironmentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.xml.transform.Source;
import java.util.List;

@Configuration
@Slf4j
@ComponentScan(basePackages = "dev.gsitgithub.webapp.controller",
        includeFilters = {@ComponentScan.Filter(Controller.class)} )
public class SpringMvcConfig extends WebMvcConfigurationSupport {

    @Value("${application.environment}")
    private String applicationEnvironment;
    @Value("${application.version}")
    private String version;
    @Inject
    EntityManagerFactory entityManagerFactory;

    @Bean
    public ExecutionTimeInterceptor executionTimeInterceptor() {
        return new ExecutionTimeInterceptor();
    }

    @Bean
    public LocaleInterceptor localeInterceptor() {
        return new LocaleInterceptor("locale");
    }

    @Bean
    public TimeZoneInterceptor timeZoneInterceptor() {
        return new TimeZoneInterceptor("timezone");
    }

    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
        OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor = new OpenEntityManagerInViewInterceptor();
        openEntityManagerInViewInterceptor.setEntityManagerFactory(entityManagerFactory);
        return openEntityManagerInViewInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(executionTimeInterceptor());
        registry.addInterceptor(localeInterceptor());
        registry.addInterceptor(timeZoneInterceptor());
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Configure the list of HttpMessageConverters to use
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new FormHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<Source>());

        // TODO: xml and json converter not used
    }

    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new JsonFormatAnnotationFormatterFactory());
        registry.addFormatterForFieldAnnotation(new ListFormatAnnotationFormatterFactory());
        registry.addFormatterForFieldAnnotation(new CustomFormatAnnotationFormatterFactory(registry));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (applicationEnvironment.equals(ApplicationEnvironmentUtils.PRODUCTION)) {
            registry.addResourceHandler("/resources-" + version + "/**")
                    .addResourceLocations("/resources/")
                    .setCachePeriod(365 * 24 * 60 * 60); // 365*24*60*60 equals one year
        } else {
            registry.addResourceHandler("/resources-" + version + "/**")
                    .addResourceLocations("/resources/")
                    .setCachePeriod(0); // Don't chache
        }
    }
/*
    @Bean
    @Override
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
        adapter.setIgnoreDefaultModelOnRedirect(true); // Makes sure url parameters are removed on a redirect
        return adapter;
    }*/

    // TODO: Start using @PathVariable in Controllers or remove from configuration
    // for an example see http://refcardz.dzone.com/refcardz/core-spring-data#refcard-download-social-buttons-display
    @Bean
    public DomainClassConverter<?> domainClassConverter() {
        return new DomainClassConverter<FormattingConversionService>(mvcConversionService());
    }

    // TODO: Start using Pageable objects in Controllers or remove from configuration
    // for an example see http://refcardz.dzone.com/refcardz/core-spring-data#refcard-download-social-buttons-display
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SortHandlerMethodArgumentResolver());
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        //registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("root");
    }

    // TODO: view resolver is not working, try Themeleaf
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }
}
