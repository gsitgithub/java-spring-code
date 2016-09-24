package dev.gsitgithub.webapp.entity;

import dev.gsitgithub.webapp.entity.abstracts.IdEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
//@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor
@Setter
@Getter
public class PreferenceValue extends IdEntity implements Serializable {
    @ManyToOne
    private Preference preference;
    private String value;
}
