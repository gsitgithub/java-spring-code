package dev.gsitgithub.dao;

import java.util.List;

import dev.gsitgithub.entity.Role;
import dev.gsitgithub.entity.UserRight;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.gsitgithub.entity.User;

@Repository
public class UserProfileDaoImpl implements UserProfileDAO  {
	
	@Autowired
    private SessionFactory sessionFactory;

	//@PreAuthorize("hasRole('ROLE_USER')")
	public void addUserProfile(User employee) {
		//System.out.println(((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities());
		this.sessionFactory.getCurrentSession().save(employee);
	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUserProfile() {
		return this.sessionFactory.getCurrentSession().createQuery("from UserProfile").list();
	}

	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteUserProfile(Integer employeeId) {
		User employee = (User) sessionFactory.getCurrentSession().load(
				User.class, employeeId);
        if (null != employee) {
        	this.sessionFactory.getCurrentSession().delete(employee);
        }
	}

	public User getUserByEmail(String email) {
		Query query = this.sessionFactory.getCurrentSession().createQuery("from dev.gsitgithub.entity.User u where u.email = :email");
		query.setParameter("email",email);
		List<User> userList = (List<User>) query.list();
		if(userList.isEmpty())
			return null;
		User userP = (User) userList.get(0); 
		for (Role role : userP.getRoles()) {
			role.getId();
			role.getName();
			for (UserRight right : role.getRights()) {
				right.getId();
				right.getName();
			}
		}
		return userP;
	}
}
