package dev.gsitgithub.webapp.entity;

import dev.gsitgithub.webapp.entity.abstracts.IdEntity;
import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RememberMeToken extends IdEntity {

    @NonNull
    @Column(length = 64)
    private String username;
    @Column(unique = true, length = 64)
    private String series;
    @NonNull
    @Column(length = 64)
    private String token;
    @NonNull
    private DateTime lastUsed;

}
