package dev.gsitgithub.repo;

import java.util.List;

import dev.gsitgithub.generic.repo.GenericRepository;
import org.springframework.stereotype.Repository;

import dev.gsitgithub.entity.UrlConfig;
import dev.gsitgithub.entity.UrlConfig.ConfigType;

@Repository
public interface UrlConfigRepository extends GenericRepository<UrlConfig, Long> {
	public List<UrlConfig> getByType(ConfigType type);
}
