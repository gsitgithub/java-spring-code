package dev.gsitgithub.webapp.entity;

import dev.gsitgithub.webapp.entity.abstracts.IdEntity;
import dev.gsitgithub.webapp.entity.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor
@Setter
@Getter
public class Group extends IdEntity implements Serializable {

    @Column(unique = true)
    @NonNull
    private String groupname;
    @ElementCollection(targetClass = Role.class, fetch = EAGER)
    @Enumerated(STRING)
    private Set<Role> groupRoles = EnumSet.noneOf(Role.class);
    @ManyToMany(cascade = ALL, fetch = LAZY)
    private Set<User> users = new HashSet();
    @OneToMany(fetch = EAGER)
    private Set<PreferenceValue> preferenceValues = new HashSet();
}    
