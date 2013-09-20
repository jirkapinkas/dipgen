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
import com.dipgen.service.JavaMailSenderService;
import com.dipgen.service.security.UserService;

@Controller
public class UserDetailController {

	@Autowired
	private UserService userService;

	@Autowired
	private JavaMailSenderService javaMailSenderService;

	@RequestMapping(value = "/user-details", method = RequestMethod.GET)
	public String show(Principal principal, Model model) {
		String name = principal.getName();
		User user = userService.findOne(name);
		model.addAttribute("user", user);
		model.addAttribute("isPremium", userService.isPremium(principal.getName()));
		return "user-details";
	}

	@RequestMapping(value = "/user-details", method = RequestMethod.POST)
	public String updateProfile(Principal principal, @ModelAttribute User userForm) {
		User user = userService.findOne(principal.getName());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());
		userService.updateWithNewPassword(user);
		return "redirect:/user-details.html?success=true";
	}

	@RequestMapping(value = "/user-details", method = RequestMethod.POST, params = "billing")
	public String updateBilling(Principal principal, @ModelAttribute User userForm) {
		User user = userService.findOne(principal.getName());
		user.setFirstName(userForm.getFirstName());
		user.setLastName(userForm.getLastName());
		user.setAddress1(userForm.getAddress1());
		user.setAddress2(userForm.getAddress2());
		user.setCity(userForm.getCity());
		user.setState(userForm.getState());
		user.setPostalCode(userForm.getPostalCode());
		user.setCountry(userForm.getCountry());
		user.setAdditionalInformation(userForm.getAdditionalInformation());
		user.setRequestPremium(true);
		userService.update(user);
		javaMailSenderService.requestPremium(principal.getName());
		return "redirect:/user-details.html?success=true&billing=true";
	}

	@RequestMapping("/user-details/delete-account")
	public String delete(Principal principal, HttpSession session) {
		userService.deactivate(principal.getName());
		session.invalidate();
		return "redirect:/index.html";
	}
}
