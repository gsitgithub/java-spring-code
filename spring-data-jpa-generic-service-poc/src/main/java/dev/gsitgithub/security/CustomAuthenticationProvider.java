package dev.gsitgithub.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import dev.gsitgithub.entity.Role;
import dev.gsitgithub.entity.User;
import dev.gsitgithub.entity.UserRight;
import dev.gsitgithub.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

//@Component(value = "customAuthenticationProvider")
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	private UserService userService;
	private final String PERMISSION_PREFIX = "ROLE_RIGHT_";
	 
    @Autowired
    public CustomAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }
	
	/*public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		System.out.println("\n\n--    Authentication DEBUG   -- "+ authentication.getPrincipal().toString() +"\n\n");
		User profile = userProfileService.findByEmail(authentication.getPrincipal().toString());
		 
        if(profile == null){
            throw new UsernameNotFoundException(String.format("Invalid credentials", authentication.getPrincipal()));
        }
 
//        String suppliedPasswordHash = DigestUtils.sha1Hex(authentication.getCredentials().toString());
        String suppliedPasswordHash = authentication.getCredentials().toString();
 
        if(!profile.getPasswordHash().equals(suppliedPasswordHash)){
            throw new BadCredentialsException("Invalid credentials");
        }
 
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(profile, null, profile.getAuthorities());
 
        return token;
	}
	
	@Override
	public boolean supports(Class<? extends Object> aClass) {
		return aClass.equals(UsernamePasswordAuthenticationToken.class);
	}*/

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authenticationToken)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("\n\nMethod invoked : additionalAuthenticationChecks isAuthenticated ? :"
				+authenticationToken.isAuthenticated()+"\n\n");
		
	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		System.out.println("\n\nMethod invoked : retrieveUser "+username);
		System.out.println("\n\nMethod invoked : authentication.getName "+authentication.getName());
		System.out.println("\n\nMethod invoked : authentication.getPrincipal "+authentication.getPrincipal());
		User domainUser = userService.getUserByEmail(username);
        
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
 
        return new org.springframework.security.core.userdetails.User(
                domainUser.getEmail(),
                domainUser.getPasswordHash(),
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                //getAuthorities(domainUser.getRole().getId())
                getAuthorities(domainUser)
        );
	}

//  Mymethod
	public Collection<? extends GrantedAuthority> getAuthorities(
			User user) {
		Set<UserAuthority> authorities = new HashSet<UserAuthority>();
		for (Role role : user.getRoles()) {
			for (UserRight right : role.getRights()) {
				UserAuthority produxAuthority = new UserAuthority(
						PERMISSION_PREFIX + right.getName());
				authorities.add(produxAuthority);
			}
		}
		return authorities;
	}
}

