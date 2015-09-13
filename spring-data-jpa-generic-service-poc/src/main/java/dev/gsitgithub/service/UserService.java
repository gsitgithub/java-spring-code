package dev.gsitgithub.service;

import dev.gsitgithub.entity.User;
import dev.gsitgithub.generic.service.GenericService;

public interface UserService extends GenericService<User, Long> {
    public User getUserByEmail(String email);
}
