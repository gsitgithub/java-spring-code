package dev.gsitgithub.services;

import javax.transaction.Transactional;

import dev.gsitgithub.generic.service.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.gsitgithub.entity.User;
import dev.gsitgithub.repo.UserRepository;

@Service
@Transactional
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {
	private UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		super(userRepository);
		this.userRepository = userRepository;
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}

	@Override
	public User getUserByUsername(String username) {
		return userRepository.getUserByUsername(username);
	}

	@Override
	public User getUserByUsernameAndPasswordHash(String username,
			String password) {
		return userRepository.getUserByUsernameAndPasswordHash(username, password);
	}

}
