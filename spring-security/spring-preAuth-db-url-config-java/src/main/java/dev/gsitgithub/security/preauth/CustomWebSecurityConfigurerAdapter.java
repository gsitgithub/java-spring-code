package dev.gsitgithub.security.preauth;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import dev.gsitgithub.dao.UrlConfigDAO;
import dev.gsitgithub.entity.UrlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.header.writers.XContentTypeOptionsHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableWebSecurity
@Configuration
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	@Autowired
	private UrlConfigDAO urlConfigDAO;
	
	private AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> customFilterDetailsService;
	
	@Autowired
	public void setCustomFilterDetailsService(AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> customFilterDetailsService) {
		this.customFilterDetailsService = customFilterDetailsService;
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		/*http.jee().disable();
		http.formLogin().disable();
		http.httpBasic().disable();
		http.portMapper().disable();
		http.rememberMe().disable();
		http.sessionManagement().disable();
		http.x509().disable();*/

		// Entry Point
		LoginUrlAuthenticationEntryPoint entryPoint = new LoginUrlAuthenticationEntryPoint("/accessdenied");
		http.exceptionHandling().authenticationEntryPoint(entryPoint);
		
		// Provider
		PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(customFilterDetailsService);
		provider.afterPropertiesSet();
		
		// AuthenticationManager
	    List<AuthenticationProvider> providers = Arrays.<AuthenticationProvider>asList(provider);
	    AuthenticationManager authenticationManager = new ProviderManager(providers);
	    
	    // Filter
	    CustomPreAuthenticatedProcessingFilter preAuthFilter = new CustomPreAuthenticatedProcessingFilter();
	    preAuthFilter.setAuthenticationManager(authenticationManager);
		preAuthFilter.afterPropertiesSet();
	    
	    http.addFilter(preAuthFilter)
			.authenticationProvider(provider);
		
		http.headers()
		  .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
		  .addHeaderWriter(new XContentTypeOptionsHeaderWriter())
		  .addHeaderWriter(new XXssProtectionHeaderWriter())
		  .addHeaderWriter(new CacheControlHeadersWriter())
		  .addHeaderWriter(new HstsHeaderWriter());
		
		http.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/logoutpage")
			.deleteCookies("JSESSIONID")
			.deleteCookies()
			.invalidateHttpSession(true)
			.permitAll();
		
		String[] filesToLetThroughUnAuthorized = { "/accessdenied", "/logoutpage", "/favicon.ico" };
		filesToLetThroughUnAuthorized = getUrlByConfigType(UrlConfig.ConfigType.PERMIT_ALL);
		String[] urlsToAuthorized = getUrlByConfigType(UrlConfig.ConfigType.AUTHENTICATED);
		
		System.err.println(Arrays.toString(getUrlByConfigType(UrlConfig.ConfigType.PERMIT_ALL)));
		System.err.println(Arrays.toString(getUrlByConfigType(UrlConfig.ConfigType.AUTHENTICATED)));
		
		http.authorizeRequests()
			.antMatchers(filesToLetThroughUnAuthorized).permitAll()
			.antMatchers(urlsToAuthorized).authenticated()
			.anyRequest().authenticated();
	}
	
	private String[] getUrlByConfigType(UrlConfig.ConfigType type){
		List<UrlConfig> urlConfigs= urlConfigDAO.getUrlConfigByType(type);
		String [] urls = new String[urlConfigs.size()];
		for (int idx = 0; idx < urlConfigs.size(); idx++) {
			UrlConfig config = urlConfigs.get(idx);
			urls[idx] = config.getUrl();
		}
		return urls;
	}
	
//	@Bean
	public FilterChainProxy springSecurityFilterChain() throws Exception {
	    // AuthenticationEntryPoint
	    BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
	    entryPoint.setRealmName("AppName Realm");
	    
	    // accessDecisionManager
	    List<AccessDecisionVoter> voters = Arrays.<AccessDecisionVoter>asList(new RoleVoter(), new WebExpressionVoter());
	    AccessDecisionManager accessDecisionManager = new AffirmativeBased(voters);
	    
	    // SecurityExpressionHandler
	    SecurityExpressionHandler<FilterInvocation> securityExpressionHandler = new DefaultWebSecurityExpressionHandler();
	    
	    // AuthenticationUserDetailsService
//	    AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService = new AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>(customFilterDetailsService);
//	    authenticationUserDetailsService.afterPropertiesSet();
	    
	    // PreAuthenticatedAuthenticationProvider
	    PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
	    preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(customFilterDetailsService);
	    preAuthenticatedAuthenticationProvider.afterPropertiesSet();
	    
	    // AuthenticationManager
	    List<AuthenticationProvider> providers = Arrays.<AuthenticationProvider>asList(preAuthenticatedAuthenticationProvider);
	    AuthenticationManager authenticationManager = new ProviderManager(providers);
	    
	    // HttpSessionSecurityContextRepository
	    HttpSessionSecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();
	    
	    // SessionRegistry
	    SessionRegistry sessionRegistry = new SessionRegistryImpl();
	    
	    // ConcurrentSessionControlStrategy
	    ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);

	    // ConcurrentSessionFilter
	    ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry);
	    concurrentSessionFilter.afterPropertiesSet();
	    
	    // SecurityContextPersistenceFilter
	    SecurityContextPersistenceFilter securityContextPersistenceFilter = new SecurityContextPersistenceFilter(httpSessionSecurityContextRepository);
	    
	    // X509AuthenticationFilter
	    X509AuthenticationFilter x509AuthenticationFilter = new X509AuthenticationFilter();
	    x509AuthenticationFilter.setAuthenticationManager(authenticationManager);
	    x509AuthenticationFilter.afterPropertiesSet();
	    
	    // RequestCacheAwareFilter
	    RequestCacheAwareFilter requestCacheAwareFilter = new RequestCacheAwareFilter();
	    
	    // SecurityContextHolderAwareRequestFilter
	    SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter = new SecurityContextHolderAwareRequestFilter();
	    
	    // SessionManagementFilter
	    SessionManagementFilter sessionManagementFilter = new SessionManagementFilter(httpSessionSecurityContextRepository, concurrentSessionControlStrategy);
	    
	    // ExceptionTranslationFilter
	    ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(entryPoint);
	    exceptionTranslationFilter.setAccessDeniedHandler(new AccessDeniedHandlerImpl());
	    exceptionTranslationFilter.afterPropertiesSet();
	    
	    // FilterSecurityInterceptor
	    FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
	    filterSecurityInterceptor.setAuthenticationManager(authenticationManager);
	    filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager);
	    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> map = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
	    map.put(new AntPathRequestMatcher("/**"), Arrays.<ConfigAttribute>asList(new SecurityConfig("isAuthenticated()")));
	    ExpressionBasedFilterInvocationSecurityMetadataSource ms = new ExpressionBasedFilterInvocationSecurityMetadataSource(map, securityExpressionHandler);
	    filterSecurityInterceptor.setSecurityMetadataSource(ms);
	    filterSecurityInterceptor.afterPropertiesSet();
	    
	    // SecurityFilterChain
	    SecurityFilterChain chain = new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"),
	            concurrentSessionFilter,
	            securityContextPersistenceFilter,
	            x509AuthenticationFilter,
	            requestCacheAwareFilter,
	            securityContextHolderAwareRequestFilter,
	            sessionManagementFilter,
	            exceptionTranslationFilter,
	            filterSecurityInterceptor);
	    return new FilterChainProxy(chain);
	}
}