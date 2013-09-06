package com.dipgen.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dipgen.entity.security.Role;
import com.dipgen.entity.security.Role.ROLE_TYPE;
import com.dipgen.repository.security.RoleRepository;

@Service
@Transactional
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public Role save(Role role) {
		return roleRepository.save(role);
	}

	public Role findOne(int roleId) {
		return roleRepository.findOne(roleId);
	}

	public Role findOne(ROLE_TYPE name) {
		return roleRepository.findByName(name);
	}
}
