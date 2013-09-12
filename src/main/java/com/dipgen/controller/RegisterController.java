package com.dipgen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dipgen.entity.security.Role;
import com.dipgen.entity.security.Role.ROLE_TYPE;
import com.dipgen.entity.security.User;
import com.dipgen.service.security.RoleService;
import com.dipgen.service.security.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@ModelAttribute("user")
	public User construct() {
		return new User();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String show() {
		return "register";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute User user) {
		user = userService.create(user);
		Role roleUser = roleService.findOne(ROLE_TYPE.ROLE_USER);
		userService.assignRole(user.getUserId(), roleUser.getRoleId());
		return "redirect:/register.html?success=true";
	}

	@RequestMapping("/check")
	@ResponseBody
	public String check(@RequestParam String name) {
		if (userService.findOne(name) != null) {
			return "selected";
		} else {
			return "free";
		}
	}

}
