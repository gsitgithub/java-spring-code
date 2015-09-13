package dev.gsitgithub.services;

import java.util.List;

import dev.gsitgithub.generic.service.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.gsitgithub.entity.UrlConfig;
import dev.gsitgithub.entity.UrlConfig.ConfigType;
import dev.gsitgithub.repo.UrlConfigRepository;

@Service
public class UrlConfigServiceImpl extends GenericServiceImpl<UrlConfig, Long> implements UrlConfigService {
	private UrlConfigRepository urlConfigRepository;
	
	@Autowired
	public UrlConfigServiceImpl(UrlConfigRepository urlConfigRepository) {
		super(urlConfigRepository);
		this.urlConfigRepository = urlConfigRepository;
	}

	@Override
	public List<UrlConfig> getByType(ConfigType type) {
		return urlConfigRepository.getByType(type);
	}

}
