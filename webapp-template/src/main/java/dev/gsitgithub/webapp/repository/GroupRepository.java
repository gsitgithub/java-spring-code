package dev.gsitgithub.webapp.repository;

import dev.gsitgithub.webapp.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findByGroupname(String groupname);

    Group findByIdNotAndGroupname(Long id, String groupname);

    Page<Group> findByGroupnameLike(Long groupname, Pageable pageable);

}
