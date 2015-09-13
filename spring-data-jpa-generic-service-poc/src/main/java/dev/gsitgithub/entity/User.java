package dev.gsitgithub.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import dev.gsitgithub.generic.repo.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import dev.gsitgithub.security.UserAuthority;

@Entity //(name = "User")
@Table(name="USER")
public class User implements BaseEntity {
     
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

 
    @Column(name="EMAIL")
    private String email;
     
    @Column(name="PASSWORDHASH")
    private String passwordHash;
    
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name="USER_ROLE",
    	joinColumns={ @JoinColumn(name="USER_ID")},
    	inverseJoinColumns={@JoinColumn(name="ROLE_ID")})
    private Set<Role> roles = new HashSet<Role>();
    
    @Transient
    private final String PERMISSION_PREFIX = "ROLE_RIGHT_";
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPasswordHash() {
		return passwordHash;
	}
    public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
/*    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            authorities = ((User) principal).getAuthorities();
        } else {
            System.err.println("Principal is not an instance of com.dtr.oas.model.User");
        }
        return authorities;
    }*/
    
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<UserAuthority> authorities = new HashSet<UserAuthority>();
        for (Role role : roles) {
            for (UserRight right : role.getRights()) {
            	UserAuthority produxAuthority = new UserAuthority(PERMISSION_PREFIX + right.getName());
                authorities.add(produxAuthority);
            }
        }
        return authorities;
    }
}