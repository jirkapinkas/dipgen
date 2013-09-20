package com.dipgen.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dipgen.service.JavaMailSenderService;
import com.dipgen.service.security.UserService;

@Controller
public class ContactController {

	@Autowired
	private JavaMailSenderService javaMailSenderService;

	@Autowired
	private UserService userService;

	@RequestMapping("/contact")
	public String show() {
		return "contact";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.POST)
	public String post(Principal principal, @RequestParam(required = false) String email, @RequestParam String question) {
		if (principal != null) {
			email = userService.findOne(principal.getName()).getEmail();
		}
		String name = null;
		if(principal != null) {
			name = principal.getName();
		} else {
			name = "<unregistered user>";
		}
		javaMailSenderService.contactForm(email, name, question);
		return "redirect:/contact.html?sent=true";
	}

}
