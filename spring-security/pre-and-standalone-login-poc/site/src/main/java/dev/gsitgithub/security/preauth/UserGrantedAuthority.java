package dev.gsitgithub.security.preauth;

import org.springframework.security.core.GrantedAuthority;

public class UserGrantedAuthority  implements GrantedAuthority {

	private static final long serialVersionUID = 7985362233432398338L;
	private String authority;
	 
    public UserGrantedAuthority(String authority) {
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
        if(!(obj instanceof UserGrantedAuthority)) return false;
        return ((UserGrantedAuthority) obj).getAuthority().equals(authority);
    }
}
