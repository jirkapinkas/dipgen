package com.dipgen.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dipgen.service.security.UserService;

@Controller
public class UsersController {

	@Autowired
	private UserService userService;

	@RequestMapping("/admin/users")
	public String show(Model model) {
		model.addAttribute("users", userService.findAll());
		return "admin/users";
	}
	
	@RequestMapping("/admin/users/delete")
	public String delete(@RequestParam int userId) {
		userService.delete(userId);
		return "redirect:/admin/users.html";
	}
}
