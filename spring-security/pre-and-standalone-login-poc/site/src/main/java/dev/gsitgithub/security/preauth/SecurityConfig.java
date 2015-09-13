package dev.gsitgithub.security.preauth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import dev.gsitgithub.entity.UrlConfig;
import dev.gsitgithub.entity.UrlConfig.ConfigType;
import dev.gsitgithub.services.UrlConfigService;

/**
 * Created by PPotnis on 05-01-2015.
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private Logger Log = LoggerFactory.getLogger(getClass());
	private List<UrlConfig> urlConfigs;
	@Autowired
	private AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> userDetailsService;

	@Resource
	private UrlConfigService urlConfigService;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(getUrlByConfigType(ConfigType.WEB_RESOURCES));
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic().disable();
		http.formLogin().disable();
		http.headers().disable();
		
		Log.info(Arrays.toString(getUrlByConfigType(UrlConfig.ConfigType.PERMIT_ALL)));
		Log.info(Arrays.toString(getUrlByConfigType(UrlConfig.ConfigType.AUTHENTICATED)));
		
		String[] filesToLetThroughUnAuthorized = getUrlByConfigType(ConfigType.PERMIT_ALL);
		String[] urlsToAuthorized = getUrlByConfigType(UrlConfig.ConfigType.AUTHENTICATED);
		
		http.authorizeRequests()
			.antMatchers(filesToLetThroughUnAuthorized).permitAll()
			.antMatchers(urlsToAuthorized).authenticated();
		
		http.logout().logoutUrl(getUrlByConfigType(UrlConfig.ConfigType.LOGOUT_URL)[0]).permitAll()
			.logoutSuccessUrl(getUrlByConfigType(UrlConfig.ConfigType.LOGOUT_SUCCESS_URL)[0]).permitAll()
			.deleteCookies("JSESSIONID").deleteCookies()
			.invalidateHttpSession(true).permitAll();
		
		// Entry Point
		LoginUrlAuthenticationEntryPoint entryPoint = 
				new LoginUrlAuthenticationEntryPoint(getUrlByConfigType(UrlConfig.ConfigType.ENTRY_POINT_URL)[0]);
		http.exceptionHandling().authenticationEntryPoint(entryPoint);
		
		// Provider
		PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
		provider.setPreAuthenticatedUserDetailsService(userDetailsService);
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
	    
	}
	
	private String[] getUrlByConfigType(ConfigType type){
		List<String> urls = new ArrayList<String>();
		if(urlConfigs == null)
			urlConfigs= urlConfigService.findAll();
		for (UrlConfig urlC : urlConfigs) {
			if(type.toString().equals(urlC.getType().toString()))
				urls.add(urlC.getUrl());
		}
		return urls.toArray(new String[urls.size()]);
	}
}
