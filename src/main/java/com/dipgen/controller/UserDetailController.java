package com.dipgen.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dipgen.entity.security.User;
import com.dipgen.service.security.UserService;

@Controller
public class UserDetailController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user-details", method = RequestMethod.GET)
	public String show(Principal principal, Model model) {
		String name = principal.getName();
		User user = userService.findOne(name);
		model.addAttribute("user", user);
		return "user-details";
	}

	@RequestMapping(value = "/user-details", method = RequestMethod.POST)
	public String update(Principal principal, @ModelAttribute User userForm) {
		User user = userService.findOne(principal.getName());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());
		userService.updateWithNewPassword(user);
		return "redirect:/user-details.html?success=true";
	}

	@RequestMapping("/user-details/delete-account")
	public String delete(Principal principal, HttpSession session) {
		userService.deactivate(principal.getName());
		session.invalidate();
		return "redirect:/index.html";
	}
}
