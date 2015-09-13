package dev.gsitgithub.repo;

import dev.gsitgithub.entity.User;
import dev.gsitgithub.generic.repo.GenericRepository;

public interface UserRepository extends GenericRepository<User, Long> {
	User getUserByEmail(String email); 
	User getUserByUsername(String username); 
	User getUserByUsernameAndPasswordHash(String username, String password); 
}
