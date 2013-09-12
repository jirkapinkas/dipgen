package com.dipgen.service;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dipgen.entity.Diploma;
import com.dipgen.entity.security.Role;
import com.dipgen.entity.security.Role.ROLE_TYPE;
import com.dipgen.entity.security.User;
import com.dipgen.service.security.RoleService;
import com.dipgen.service.security.UserService;
import com.dipgen.service.util.ImageUtil;
import com.dipgen.service.util.SvgUtil;

@Service
public final class DbInitService {

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private DiplomaService diplomaService;

	@Value("${server.url}")
	private String serverUrl;

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

			try {
				Diploma diploma = new Diploma();
				diploma.setName("retro diploma");
				String svg = SvgUtil.normalizeSvg(IOUtils.readLines(getClass().getResourceAsStream("/diploma1.svg")));
				svg = ImageUtil.embeddImages(IOUtils.toInputStream(svg));
				diploma.setSvg(svg);
				diploma.setUser(userGuest);
				diplomaService.save(diploma);
			} catch (IOException e) {
				System.err.println("could not read diploma 1 :(");
				e.printStackTrace();
			}

			System.out.println("*** CREATE TEST DATA FINISH ***");

		}
	}

}
