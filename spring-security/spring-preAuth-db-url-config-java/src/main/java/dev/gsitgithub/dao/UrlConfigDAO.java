package dev.gsitgithub.dao;

import java.util.List;

import dev.gsitgithub.entity.UrlConfig;

public interface UrlConfigDAO 
{
    public void addUrlConfig(UrlConfig urlConfig);
    public List<UrlConfig> getAllUrlConfig();
    public void deleteUrlConfig(Integer id);
	public List<UrlConfig> getUrlConfigByType(UrlConfig.ConfigType type);
}