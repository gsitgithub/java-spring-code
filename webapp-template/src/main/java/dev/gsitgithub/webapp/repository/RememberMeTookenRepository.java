package dev.gsitgithub.webapp.repository;

import dev.gsitgithub.webapp.entity.RememberMeToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RememberMeTookenRepository extends JpaRepository<RememberMeToken, Long> {

    RememberMeToken findBySeries(String series);

    List<RememberMeToken> findByUsername(String username);

}
