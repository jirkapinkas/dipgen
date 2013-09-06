package com.dipgen.repository.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dipgen.entity.security.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select distinct u from User u left join fetch u.roles")
	List<User> findAllUsersWithRoles();
	
	User findByName(String name);
}
