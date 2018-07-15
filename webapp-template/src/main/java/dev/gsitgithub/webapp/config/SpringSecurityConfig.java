package dev.gsitgithub.webapp.config;

import dev.gsitgithub.webapp.config.logging.MDCUsernameInsertingFilter;
import dev.gsitgithub.webapp.config.springsecurity.CustomWebSecurityExpressionHandler;
import dev.gsitgithub.webapp.config.springsecurity.SpringDataTokenRepositoryImpl;
import dev.gsitgithub.webapp.config.springsecurity.UserDetailsServiceAnonymousAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@Profile(WebXmlConfig.PROFILE_SECURITY)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    static final String REMEMBER_ME_KEY = "78780c25-1849-4796-a79c-0f4326f32dfd";

    @Inject
    UserDetailsService userDetailService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .expressionHandler(webSecurityExpressionHandler())
            .ignoring()
                .antMatchers("/resources/**")
                .antMatchers("/resources-*/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .headers() // TODO: (REWRITE) This enables opening javamelody in an iframe, see https://jira.spring.io/browse/SEC-2501 and https://jira.spring.io/browse/SPR-11496
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
            // enable https forwarding
//            .requiresChannel()
//                .anyRequest().requiresSecure()
//                .and()
            .authorizeRequests()
                .expressionHandler(webSecurityExpressionHandler())
                .antMatchers("/database/**").access("hasRole('ROLE_DEVELOPER') and !isProductionEnvironment()")
                .antMatchers("/javamelody/**").access("hasRole('ROLE_DEVELOPER')")
                .and()
            .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/login/failure")
                .defaultSuccessUrl("/login/success")
                .and()
            .logout()
                .logoutUrl("/normallogout")
                .logoutSuccessUrl("/logout/success")
                .invalidateHttpSession(true)
                .and()
            .rememberMe()
                .tokenRepository(springDataTokenRepository())
                .useSecureCookie(true)
                .and()
            .exceptionHandling()
                .accessDeniedPage("/accessdenied")
                .and()
            .anonymous().disable()
            .addFilterBefore(anonymousAuthenticationFilter(), AnonymousAuthenticationFilter.class)
            .addFilter(switchUserFilter())
            .addFilterAfter(mdcUsernameInsertingFilter(), SecurityContextPersistenceFilter.class)
            .authorizeRequests()
                .expressionHandler(webSecurityExpressionHandler())
                .antMatchers("/switchuserto").hasRole("ROOT")
                .and()
            .csrf().disable()
            .httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsServiceAnonymousAuthenticationFilter anonymousAuthenticationFilter() {
        UserDetailsServiceAnonymousAuthenticationFilter anonymousAuthenticationFilter = new UserDetailsServiceAnonymousAuthenticationFilter(userDetailService);
        return anonymousAuthenticationFilter;
    }

    @Bean
    public SwitchUserFilter switchUserFilter() {
        SwitchUserFilter switchUserFilter = new SwitchUserFilter();
        switchUserFilter.setUserDetailsService(userDetailService);
        switchUserFilter.setTargetUrl("/");
        switchUserFilter.setSwitchUserUrl("/switchuserto");
        switchUserFilter.setUsernameParameter("username");
        switchUserFilter.setExitUserUrl("/switchuserlogout");
        return switchUserFilter;
    }

    @Bean
    public MDCUsernameInsertingFilter mdcUsernameInsertingFilter() {
        MDCUsernameInsertingFilter mdcUsernameInsertingFilter = new MDCUsernameInsertingFilter();
        return mdcUsernameInsertingFilter;
    }

    @Bean
    public SpringDataTokenRepositoryImpl springDataTokenRepository() {
        SpringDataTokenRepositoryImpl springDataTokenRepository = new SpringDataTokenRepositoryImpl();
        return springDataTokenRepository;
    }

    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        RememberMeAuthenticationProvider rememberMeAuthenticationProvider = new RememberMeAuthenticationProvider(REMEMBER_ME_KEY);
        return rememberMeAuthenticationProvider;
    }

    @Bean
    public CustomWebSecurityExpressionHandler webSecurityExpressionHandler() {
        CustomWebSecurityExpressionHandler customWebSecurityExpressionHandler = new CustomWebSecurityExpressionHandler();
        return customWebSecurityExpressionHandler;
    }
}
