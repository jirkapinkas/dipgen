package com.dipgen.service;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dipgen.entity.Diploma;
import com.dipgen.entity.GeneratorString;
import com.dipgen.entity.GeneratorString.HtmlComponentType;
import com.dipgen.entity.security.Role;
import com.dipgen.entity.security.Role.ROLE_TYPE;
import com.dipgen.entity.security.User;
import com.dipgen.service.security.RoleService;
import com.dipgen.service.security.UserService;
import com.dipgen.service.util.DiplomaUtil;
import com.dipgen.service.util.ImageUtil;
import com.dipgen.service.util.SvgUtil;

@Service
public class DiplomaInitService {

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private DiplomaService diplomaService;

	@Autowired
	private GeneratorService generatorService;

	@Value("${server.url}")
	private String serverUrl;

	/**
	 * Add to user some sample diplomas
	 * 
	 * @param user
	 *            User
	 * @throws IOException
	 */
	@Async
	public void initUser(User user) {
		String serverUrl = this.serverUrl;
		if (!serverUrl.endsWith("/")) {
			serverUrl += "/";
		}
		serverUrl += "demo/image.html?name=";
		initUser(user, serverUrl);
	}

	private void initDiploma(User user, String svgName, String diplomaName, String imagesLocation, boolean embeddImages) {
		InputStream stream = null;
		try {
			stream = getClass().getResourceAsStream(svgName);
			Diploma diploma = new Diploma();
			diploma.setName(diplomaName);
			String svg = SvgUtil.normalizeSvg(IOUtils.readLines(stream));
			svg = svg.replace("{IMAGES-URL}", imagesLocation);
			if (embeddImages) {
				InputStream embeddImagesStream = IOUtils.toInputStream(svg);
				svg = ImageUtil.embeddImages(embeddImagesStream);
				embeddImagesStream.close();
			}
			diploma.setSvg(svg);
			diploma.setUser(user);
			diploma = diplomaService.save(diploma);
			GeneratorString gsDiplomaName = new GeneratorString();
			gsDiplomaName.setDiploma(diploma);
			gsDiplomaName.setHtmlComponentType(HtmlComponentType.TEXTFIELD);
			gsDiplomaName.setString("diploma name");
			generatorService.save(gsDiplomaName);

			GeneratorString gsDate = new GeneratorString();
			gsDate.setDiploma(diploma);
			gsDate.setHtmlComponentType(HtmlComponentType.TEXTFIELD);
			gsDate.setString("date");
			generatorService.save(gsDate);

			GeneratorString gsPersonName = new GeneratorString();
			gsPersonName.setDiploma(diploma);
			gsPersonName.setHtmlComponentType(HtmlComponentType.TEXTAREA);
			gsPersonName.setString("person name");
			generatorService.save(gsPersonName);
		} catch (IOException ex) {
			throw new DiplomaUtil.SvgConversionException("could not create user");
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initUser(User user, String imagesLocation) {
		boolean embeddImages = imagesLocation.startsWith("classpath:");
		initDiploma(user, "/diploma4.svg", "UP!", imagesLocation, embeddImages);
		initDiploma(user, "/diploma1.svg", "retro", imagesLocation, embeddImages);
		initDiploma(user, "/diploma2.svg", "optimistic", imagesLocation, embeddImages);
		initDiploma(user, "/diploma3.svg", "children", imagesLocation, embeddImages);
	}

	private int initDatabase() {
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
		userAdmin.setEmail("admin@email.com");
		userAdmin = userService.create(userAdmin);
		userService.assignRole(userAdmin.getUserId(), roleAdmin.getRoleId());
		userService.assignRole(userAdmin.getUserId(), roleUser.getRoleId());
		userService.assignRole(userAdmin.getUserId(), rolePremium.getRoleId());
		return roleUser.getRoleId();
	}

	private void createGuest(int roleUserId) {
		User userGuest = new User();
		userGuest.setName("guest");
		userGuest.setPassword("guest");
		userGuest.setEmail("guest@email.com");
		userGuest = userService.create(userGuest);
		userService.assignRole(userGuest.getUserId(), roleUserId);
		initUser(userGuest, "classpath:/images/");
	}

	@PostConstruct
	public void init() {
		// init database and create test database
		String hbm2ddlValue = (String) entityManagerFactory.getProperties().get("hibernate.hbm2ddl.auto");
		if ("create".equals(hbm2ddlValue) || "create-drop".equals(hbm2ddlValue)) {
			System.out.println("*** CREATE TEST DATABASE START ***");
			int roleUserId = initDatabase();
			createGuest(roleUserId);
			System.out.println("*** CREATE TEST DATABASE FINISH ***");
		} else {
			if (userService.findOne("admin") == null) {
				System.out.println("*** INIT DATABASE START ***");
				initDatabase();
				System.out.println("*** INIT DATABASE FINISH ***");
			}
		}
	}

}
