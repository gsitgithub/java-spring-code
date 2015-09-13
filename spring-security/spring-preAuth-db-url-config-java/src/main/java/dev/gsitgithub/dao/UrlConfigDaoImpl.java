package dev.gsitgithub.dao;

import java.util.ArrayList;
import java.util.List;

import dev.gsitgithub.entity.UrlConfig;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

//@Repository
@Transactional
public class UrlConfigDaoImpl implements UrlConfigDAO  {
	
	@Autowired
    private SessionFactory sessionFactory;

	//@PreAuthorize("hasRole('ROLE_RIGHT_HOME')")
	public void addUrlConfig(UrlConfig urlConfig) {
		this.sessionFactory.getCurrentSession().save(urlConfig);
	}

	@SuppressWarnings("unchecked")
	public List<UrlConfig> getAllUrlConfig() {
		return this.sessionFactory.getCurrentSession().createQuery("from UrlConfig").list();
	}

	//@PreAuthorize("hasRole('ROLE_RIGHT_EMPLOYEE')")
	public void deleteUrlConfig(Integer id) {
		UrlConfig urlConfig = (UrlConfig) sessionFactory.getCurrentSession().load(
				UrlConfig.class, id);
        if (null != urlConfig ) {
        	this.sessionFactory.getCurrentSession().delete(urlConfig);
        }
	}

	public List<UrlConfig> getUrlConfigByType(UrlConfig.ConfigType type) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from dev.gsitgithub.entity.UrlConfig u where u.type = :type");
		query.setParameter("type",type);
		List<UrlConfig> userList = (List<UrlConfig>) query.list();
		if(userList.isEmpty())
			return new ArrayList<UrlConfig>();
		return userList;
	}
}
