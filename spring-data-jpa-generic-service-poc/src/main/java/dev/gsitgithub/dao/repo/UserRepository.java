package dev.gsitgithub.dao.repo;

import dev.gsitgithub.entity.User;
import dev.gsitgithub.generic.api.GenericRepository;

public interface UserRepository extends GenericRepository<User, Long>
{
	public User getUserByEmail(String email);
}