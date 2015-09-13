package dev.gsitgithub.security;

import org.springframework.security.core.GrantedAuthority;

public class UserAuthority  implements GrantedAuthority {

	private static final long serialVersionUID = 7985362233432398338L;
	private String authority;
	 
    public UserAuthority(String authority) {
        this.authority = authority;
    }
 
    public String getAuthority() {
        return authority;
    }
 
    @Override
    public int hashCode() {
        return authority.hashCode();
    }
 
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof UserAuthority)) return false;
        return ((UserAuthority) obj).getAuthority().equals(authority);
    }
}
