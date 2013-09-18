package com.dipgen.service.security;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dipgen.entity.security.Role;
import com.dipgen.entity.security.Role.ROLE_TYPE;
import com.dipgen.entity.security.User;
import com.dipgen.repository.security.RoleRepository;
import com.dipgen.repository.security.UserRepository;

@SuppressWarnings("deprecation")
@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	public List<User> findAll() {
		return userRepository.findAllUsersWithRoles();
	}

	public User create(User user) {
		user.setEnabled(true);
		user.setRegistrationDate(new Date());
		PasswordEncoder encoder = new Md5PasswordEncoder();
		String hashedPass = encoder.encodePassword(user.getPassword(), null);
		user.setPassword(hashedPass);
		return userRepository.save(user);
	}

	public User update(User user) {
		return userRepository.save(user);
	}

	public User updateWithNewPassword(User user) {
		PasswordEncoder encoder = new Md5PasswordEncoder();
		String hashedPass = encoder.encodePassword(user.getPassword(), null);
		user.setPassword(hashedPass);
		return userRepository.save(user);
	}

	public User findOne(int userId) {
		return userRepository.findOne(userId);
	}

	public User findOne(String name) {
		return userRepository.findByName(name);
	}

	public void assignRole(int userId, int roleId) {
		User user = userRepository.findOne(userId);
		user.getRoles().add(roleRepository.findOne(roleId));
	}

	public boolean isPremium(String username) {
		User user = userRepository.findByName(username);
		List<Role> userRoles = user.getRoles();
		for (Role role : userRoles) {
			if (role.getName() == ROLE_TYPE.ROLE_PREMIUM) {
				return true;
			}
		}
		return false;
	}

	public void delete(int userId) {
		userRepository.delete(userId);
	}

	public void deactivate(String name) {
		User user = userRepository.findByName(name);
		user.setEnabled(false);
	}
}
