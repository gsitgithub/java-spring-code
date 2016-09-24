package dev.gsitgithub.webapp.config.auditing;

import dev.gsitgithub.webapp.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@RevisionEntity(RevListener.class)
public class RevEntity extends DefaultRevisionEntity {

    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    @ManyToOne
    private User user;
}