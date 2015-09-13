package dev.gsitgithub.security.preauth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.gsitgithub.entity.Role;
import dev.gsitgithub.entity.UserRight;
import dev.gsitgithub.services.UserService;

@Service
@Transactional(readOnly = true)
public class PreAuthUserDetailsService implements
		AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	@Autowired
    private UserService userService;
	
	private final String PERMISSION_PREFIX = "ROLE_";

	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token)
			throws UsernameNotFoundException {
		UserDetails userDetails = null;

		boolean principal = Boolean.valueOf(token.getPrincipal().toString());
		String[] credentials = (String[]) token.getCredentials();
	    
		if (principal && credentials != null) {
	        String username = credentials[0];
            String password = credentials[1];
            if(username != null && !username.isEmpty() && password != null && !password.isEmpty())
            	userDetails = getUserDetails(username, password);
	    }
		
		if (userDetails == null)
			throw new UsernameNotFoundException("Bad credentials: " + token.getName()+" "+
					credentials[0]+" "+ credentials[1]);
		return userDetails;
	}
	
	private UserDetails getUserDetails(String userId, String password) {
		dev.gsitgithub.entity.User domainUser = userService.getUserByUsernameAndPasswordHash(userId, password);
        if(domainUser == null)
        	return null;
        
	    return new User(userId, "notused", domainUser.getEnabled(), true, true, true,
	    		getAuthorities(domainUser));
	}
	
    private Collection<? extends GrantedAuthority> getAuthorities(dev.gsitgithub.entity.User user) {
        Set<UserGrantedAuthority> authorities = new HashSet<UserGrantedAuthority>();
        if(!user.getRoles().isEmpty())
	        for (Role role : user.getRoles()) {
	            for (UserRight right : role.getRights()) {
	                authorities.add(new UserGrantedAuthority(PERMISSION_PREFIX + right.getName()));
//	            	System.out.println("\n"+PERMISSION_PREFIX + right.getName());
	            }
	        }
        return authorities;
    }
    
}
