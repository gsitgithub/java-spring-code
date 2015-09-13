package dev.gsitgithub.service;

import java.util.List;

import dev.gsitgithub.entity.User;

public interface UserProfileService {
	public void addUserProfile(User employee);
    public List<User> getAllEmployees();
    public void deleteUserProfile(Integer employeeId);
    public User findByEmail(String email);
}
