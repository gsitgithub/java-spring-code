package dev.gsitgithub.security.preauth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import dev.gsitgithub.entity.Role;
import dev.gsitgithub.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.gsitgithub.entity.UserRight;
import dev.gsitgithub.security.UserAuthority;

@Service
@Transactional(readOnly = true)
public class CustomFilterDetailsService implements
		AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	@Autowired
	private UserProfileService userProfileService;
	private final String PERMISSION_PREFIX = "ROLE_RIGHT_";

	/*
	 * public UserDetails loadUserByUsername(String login) throws
	 * UsernameNotFoundException {
	 * System.out.println("\n\n -- CustomFilterDetailsService   --\n\n" +
	 * login); boolean enabled = true; boolean accountNonExpired = true; boolean
	 * credentialsNonExpired = true; boolean accountNonLocked = true;
	 * List<GrantedAuthorityImpl> set = (List<GrantedAuthorityImpl>) new
	 * HashSet<GrantedAuthorityImpl>(); set.add(new
	 * GrantedAuthorityImpl("ROLE_LABSOLUTIONS")); return new User( login,
	 * "N/A", enabled, accountNonExpired, credentialsNonExpired,
	 * accountNonLocked, set ); }
	 */

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token)
			throws UsernameNotFoundException {
		UserDetails userDetails = null;

		boolean principal = Boolean.valueOf(token.getPrincipal().toString());
		String[] credentials = (String[]) token.getCredentials();
	    
		if (credentials != null && principal == true) {
	        String username = credentials[0];
            userDetails = getUser(username);
            String password = credentials[1];
	    }
		
		if (userDetails == null)
			throw new UsernameNotFoundException("Could not load user : " + token.getName());
		return userDetails;
	}
	
	private UserDetails getUser(String username) {
		dev.gsitgithub.entity.User domainUser = userProfileService.findByEmail(username);
        if(domainUser == null) {
        	throw new UsernameNotFoundException("Bad credentials");
        }
	    return new User(username, "notused", true, true, true, true,
	    		getAuthorities(domainUser));
	}
	
    private Collection<? extends GrantedAuthority> getAuthorities(dev.gsitgithub.entity.User user) {
        Set<UserAuthority> authorities = new HashSet<UserAuthority>();
        if(!user.getRoles().isEmpty())
	        for (Role role : user.getRoles()) {
	            for (UserRight right : role.getRights()) {
	            	UserAuthority produxAuthority = new UserAuthority(PERMISSION_PREFIX + right.getName());
	            	System.out.println("\n"+PERMISSION_PREFIX + right.getName());
	                authorities.add(produxAuthority);
	            }
	        }
        return authorities;
    }
}
