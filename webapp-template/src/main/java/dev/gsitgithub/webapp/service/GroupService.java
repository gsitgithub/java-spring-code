package dev.gsitgithub.webapp.service;

import dev.gsitgithub.webapp.entity.Group;
import dev.gsitgithub.webapp.repository.GroupRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class GroupService {

    @Inject
    private GroupRepository groupRepository;

    public Group create(Group group) {
        log.info("Creating {} group", group.getGroupname());

        if (groupRepository.findByGroupname(group.getGroupname()) != null) {
            throw new RuntimeException("Cannot create group with groupname  \"" + group.getGroupname() + "\" , the groupname is already in use by another group.");
        }

        // create entity
        return groupRepository.save(group);
    }

    public Group update(Group group) {
        if (groupRepository.findByIdNotAndGroupname(group.getId(), group.getGroupname()) != null) {
            throw new RuntimeException("Cannot update group with groupname  \"" + group.getGroupname() + "\" , the groupname is already in use by another group.");
        }

        // save entity
        return groupRepository.save(group);
    }

    public void delete(Group group) {
        groupRepository.delete(group);
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }
}
