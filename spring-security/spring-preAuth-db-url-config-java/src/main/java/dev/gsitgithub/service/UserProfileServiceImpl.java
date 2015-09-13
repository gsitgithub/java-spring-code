package dev.gsitgithub.service;

import java.util.List;

import dev.gsitgithub.dao.UserProfileDAO;
import dev.gsitgithub.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProfileServiceImpl implements UserProfileService {
	
	@Autowired
    private UserProfileDAO userProfileDAO;

	@Transactional
	public void addUserProfile(User employee) {
		userProfileDAO.addUserProfile(employee);
	}

	@Transactional(readOnly = true)
	public List<User> getAllEmployees() {
		return userProfileDAO.getAllUserProfile();
	}

	@Transactional
	public void deleteUserProfile(Integer employeeId) {
		userProfileDAO.deleteUserProfile(employeeId);
	}

	public void setEmployeeDAO(UserProfileDAO userProfileDAO) {
		this.userProfileDAO = userProfileDAO;
	}

	@Override
	@Transactional(readOnly = true)
	public User findByEmail(String email) {
		return userProfileDAO.getUserByEmail(email);
	}
}
