package dev.gsitgithub.webapp.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

import dev.gsitgithub.webapp.entity.abstracts.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    private ZonedDateTime lastUsed;

}
