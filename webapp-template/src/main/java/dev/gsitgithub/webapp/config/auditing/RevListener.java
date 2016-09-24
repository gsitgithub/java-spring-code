package dev.gsitgithub.webapp.config.auditing;

import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.annotation.Configurable;

import static dev.gsitgithub.webapp.config.utils.ApplicationUtils.*;

@Configurable
public class RevListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        // Set current username and user
        RevEntity revEntity = (RevEntity) revisionEntity;
        revEntity.setUsername(getUsername());
        revEntity.setUser(getUser());

    }
}