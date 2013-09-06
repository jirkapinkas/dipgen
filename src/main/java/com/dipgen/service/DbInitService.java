package com.dipgen.service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dipgen.entity.security.Role;
import com.dipgen.entity.security.User;
import com.dipgen.entity.security.Role.ROLE_TYPE;
import com.dipgen.service.security.RoleService;
import com.dipgen.service.security.UserService;

@Service
public class DbInitService {

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@PostConstruct
	public void init() {
		String hbm2ddlValue = (String) entityManagerFactory.getProperties().get("hibernate.hbm2ddl.auto");
		if ("create".equals(hbm2ddlValue) || "create-drop".equals(hbm2ddlValue)) {
			System.out.println("*** CREATE TEST DATA START ***");

			Role roleAdmin = new Role();
			roleAdmin.setName(ROLE_TYPE.ROLE_ADMIN);
			roleAdmin = roleService.save(roleAdmin);

			Role roleUser = new Role();
			roleUser.setName(ROLE_TYPE.ROLE_USER);
			roleUser = roleService.save(roleUser);

			Role rolePremium = new Role();
			rolePremium.setName(ROLE_TYPE.ROLE_PREMIUM);
			rolePremium = roleService.save(rolePremium);

			User userAdmin = new User();
			userAdmin.setName("admin");
			userAdmin.setPassword("admin");
			userAdmin.setEmail("jirka.pinkas@gmail.com");
			userAdmin = userService.create(userAdmin);
			userService.assignRole(userAdmin.getUserId(), roleAdmin.getRoleId());
			userService.assignRole(userAdmin.getUserId(), roleUser.getRoleId());
			userService.assignRole(userAdmin.getUserId(), rolePremium.getRoleId());

			User userGuest = new User();
			userGuest.setName("guest");
			userGuest.setPassword("guest");
			userGuest.setEmail("jirka.pinkas@gmail.com");
			userGuest = userService.create(userGuest);
			userService.assignRole(userGuest.getUserId(), roleUser.getRoleId());
			System.out.println("*** CREATE TEST DATA FINISH ***");

		}
	}

}
