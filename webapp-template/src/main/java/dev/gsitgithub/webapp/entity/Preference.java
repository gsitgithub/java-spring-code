package dev.gsitgithub.webapp.entity;

import dev.gsitgithub.webapp.entity.abstracts.IdEntity;
import lombok.*;

import javax.persistence.Entity;
import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor
@Setter
@Getter
public class Preference extends IdEntity implements Serializable {

    @NonNull
    private String name;
    private Boolean isGroupPreference;
    private Boolean isUserPreference;
}
