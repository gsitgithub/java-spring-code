package dev.gsitgithub.services;

import java.util.List;

import dev.gsitgithub.entity.UrlConfig;
import dev.gsitgithub.entity.UrlConfig.ConfigType;
import dev.gsitgithub.generic.service.GenericService;

public interface UrlConfigService extends GenericService<UrlConfig, Long>{
	public List<UrlConfig> getByType(ConfigType type);
}
