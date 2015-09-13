package dev.gsitgithub.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import dev.gsitgithub.entity.Role;
import dev.gsitgithub.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import dev.gsitgithub.entity.UserRight;

public class CustomAuthenticationManager implements AuthenticationManager {

	@Autowired
	private UserProfileService userProfileService;
	private final String PERMISSION_PREFIX = "ROLE_RIGHT_";
	
	// We need an Md5 encoder since our passwords in the database are Md5 encoded.
	 private ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder();
	
	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
//		String[] credentials = (String[]) auth.getPrincipal();
//		boolean principal = Boolean.valueOf(auth.getCredentials().toString());
		UserDetails user = null;
		try {
			user = getUser(auth.getName());
		} catch (Exception e) {
			throw new BadCredentialsException("User does not exists!");
		}
		
		// Compare passwords
		// Make sure to encode the password first before comparing
		/*if (passwordEncoder.isPasswordValid(user.getPassword(),
				(String) auth.getCredentials(), null) == false) {
			throw new BadCredentialsException("Wrong password!");
		}*/
		
		// Here's the main logic of this custom authentication manager
		// Username and password must be the same to authenticate
		if (auth.getName().equals(auth.getCredentials()) == true) {
			System.out.println("Entered username and password are the same!");
			throw new BadCredentialsException(
					"Entered username and password are the same!");

		} else {
			System.out.println("User details are good and ready to go");
			return new UsernamePasswordAuthenticationToken(auth.getName(),
					auth.getCredentials(), user.getAuthorities());
		}
		  
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
