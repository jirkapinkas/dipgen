package com.dipgen.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dipgen.entity.security.Role;
import com.dipgen.entity.security.Role.ROLE_TYPE;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	public Role findByName(ROLE_TYPE name);
}
