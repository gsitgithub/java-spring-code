package dev.gsitgithub.webapp.config.mvc;

import java.util.List;

import javax.inject.Inject;
import javax.xml.transform.Source;

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
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import dev.gsitgithub.webapp.config.format.custom.CustomFormatAnnotationFormatterFactory;
import dev.gsitgithub.webapp.config.format.json.JsonFormatAnnotationFormatterFactory;
import dev.gsitgithub.webapp.config.format.list.ListFormatAnnotationFormatterFactory;
import dev.gsitgithub.webapp.config.interceptors.LocaleInterceptor;
import dev.gsitgithub.webapp.config.interceptors.TimeZoneInterceptor;
import dev.gsitgithub.webapp.config.logging.ExecutionTimeInterceptor;
import dev.gsitgithub.webapp.config.utils.ApplicationEnvironmentUtils;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@ComponentScan(basePackages = "dev.gsitgithub.webapp.controller",
        includeFilters = {@ComponentScan.Filter(Controller.class), @ComponentScan.Filter(RestController.class)} )
public class SpringRestConfig extends WebMvcConfigurationSupport {

    @Value("${application.environment}")
    private String applicationEnvironment;
    @Value("${application.version}")
    private String version;
    @Inject
    OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor;

    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        return loggingFilter;
    }
    
    public ExecutionTimeInterceptor executionTimeInterceptor() {
        return new ExecutionTimeInterceptor();
    }

    public LocaleInterceptor localeInterceptor() {
        return new LocaleInterceptor("locale");
    }

    public TimeZoneInterceptor timeZoneInterceptor() {
        return new TimeZoneInterceptor("timezone");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(executionTimeInterceptor()).order(10);
        registry.addInterceptor(localeInterceptor());
        registry.addInterceptor(timeZoneInterceptor());
        if (openEntityManagerInViewInterceptor != null)
        	registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor);
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
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
        adapter.setIgnoreDefaultModelOnRedirect(true); // Makes sure url parameters are removed on a redirect
        return adapter;
    }

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
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
