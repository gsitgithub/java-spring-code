package dev.gsitgithub.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import dev.gsitgithub.entity.Role;
import dev.gsitgithub.entity.UserRight;
import dev.gsitgithub.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//@Service
@Transactional(readOnly=true)
public class CustomUserDetailsService implements UserDetailsService {
     
    @Autowired
    private UserProfileService userProfileService;
    
    private final String PERMISSION_PREFIX = "ROLE_RIGHT_";
 
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {
        System.out.println("\n\n -- CustomUserDetailsService   --\n\n" + login);
        /*System.out.println(getSession().getAttributeNames() +"\n\n");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(" -- auth.getName()   --" + auth.getName());
        System.out.println(" -- auth.getCredentials()   --" + auth.getCredentials());
        System.out.println(" -- auth.getPrincipal()   --" + auth.getPrincipal());
        System.out.println(" -- auth.getDetails()   --" + auth.getDetails() + "\n\n");*/
        
        
        dev.gsitgithub.entity.User domainUser = userProfileService.findByEmail(login);
        if(domainUser == null) {
        	throw new UsernameNotFoundException("Bad credentials");
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
 
        return new User(
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
    
 // example usage
    public static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(false); // true == allow create
    }
     
/*    public Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
        return authList;
    }
     
    public List<String> getRoles(Integer role) {
 
        List<String> roles = new ArrayList<String>();
 
        if (role.intValue() == 1) {
            roles.add("ROLE_MODERATOR");
            roles.add("ROLE_ADMIN");
        } else if (role.intValue() == 2) {
            roles.add("ROLE_MODERATOR");
        }
        return roles;
    }
     
    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
         
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
 */
    
//    Mymethod
    public Collection<? extends GrantedAuthority> getAuthorities(dev.gsitgithub.entity.User user) {
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
