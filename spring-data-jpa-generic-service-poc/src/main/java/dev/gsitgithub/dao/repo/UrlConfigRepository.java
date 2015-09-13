package dev.gsitgithub.dao.repo;

import java.util.List;

import dev.gsitgithub.entity.UrlConfig;
import dev.gsitgithub.generic.repo.GenericRepository;

public interface UrlConfigRepository extends GenericRepository<UrlConfig, Long> {
	List<UrlConfig> getUrlConfigByType(UrlConfig.ConfigType type);
}