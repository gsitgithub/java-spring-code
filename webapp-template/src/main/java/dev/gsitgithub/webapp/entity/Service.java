package dev.gsitgithub.webapp.entity;

import dev.gsitgithub.webapp.entity.abstracts.IdEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor
@Setter
@Getter
public class Service extends IdEntity implements Serializable {

    @NonNull
    private String name;
    private Boolean isSystemService;
    private Boolean isCompanyService;
    private Boolean isUserService;
    @OneToMany
    private Set<Preference> preferences = new HashSet();
}
