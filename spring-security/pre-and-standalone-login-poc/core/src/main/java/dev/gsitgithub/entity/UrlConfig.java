package dev.gsitgithub.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import dev.gsitgithub.generic.entity.BaseEntity;

@Entity 
@Table(name="URLCONFIG")
public class UrlConfig implements BaseEntity {
     
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
 
    @Column(name="NAME")
    private String name;
    
    @Column(name="URL")
    private String url;
    
    @Enumerated(EnumType.STRING)
    @Column(name="TYPE")
    private ConfigType type;

    public Integer getId() {
		return id;
	}
    public void setId(Integer id) {
		this.id = id;
	}
    public String getName() {
		return name;
	}
    public void setName(String name) {
		this.name = name;
	}
    public String getUrl() {
		return url;
	}
    public void setUrl(String url) {
		this.url = url;
	}
    public ConfigType getType() {
		return type;
	}
    public void setType(ConfigType type) {
		this.type = type;
	}
  
    public enum ConfigType {
    	PERMIT_ALL, AUTHENTICATED, RESOURCES, WEB_RESOURCES, LOGOUT_URL, LOGOUT_SUCCESS_URL, ENTRY_POINT_URL
    }
}