package dev.gsitgithub.dao;

import java.util.List;

import dev.gsitgithub.entity.User;

public interface UserProfileDAO 
{
    public void addUserProfile(User employee);
    public List<User> getAllUserProfile();
    public void deleteUserProfile(Integer employeeId);
	public User getUserByEmail(String email);
}