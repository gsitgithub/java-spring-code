package dev.gsitgithub.webapp.entity;

import dev.gsitgithub.webapp.entity.abstracts.IdEntity;
import dev.gsitgithub.webapp.entity.enums.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.ZoneId;
import java.util.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor
@Setter
@Getter
public class User extends IdEntity implements UserDetails, Serializable {

    @Column(unique = true)
    @NonNull
    private String username;
    @NonNull
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Duration automaticLogoutTime;
    private Locale locale;
    private ZoneId timeZone;
    private String dateFormat = "yyyy/MM/dd";
    private String timeFormat = "HH.mm";
    @ElementCollection(targetClass = Role.class, fetch = EAGER)
    @Enumerated(STRING)
    private Set<Role> userRoles = EnumSet.noneOf(Role.class);
    @ManyToMany(mappedBy = "users", fetch = EAGER)
    private Set<Group> groups = new HashSet<Group>();
    @OneToMany(fetch = EAGER)
    private Set<PreferenceValue> preferenceValues = new HashSet<PreferenceValue>();

    public Set<Role> getRoles() {
        Set<Role> roles = EnumSet.noneOf(Role.class);
        roles.addAll(getUserRoles());
        roles.addAll(getGroupRoles());
        return roles;
    }

    private Set<Role> getGroupRoles() {
        Set<Role> roles = EnumSet.noneOf(Role.class);
        for (Group group : getGroups()) {
            roles.addAll(group.getGroupRoles());
        }
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new LinkedHashSet<>();
        for (Role role : getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return grantedAuthorities;
    }
}
