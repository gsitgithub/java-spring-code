package dev.gsitgithub.services;

import dev.gsitgithub.entity.User;
import dev.gsitgithub.generic.service.GenericService;

public interface UserService extends GenericService<User, Long> {
    User getUserByEmail(String email);
	User getUserByUsername(String username); 
	User getUserByUsernameAndPasswordHash(String username, String password);
}
