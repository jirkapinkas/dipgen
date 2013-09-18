package com.dipgen.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

	@RequestMapping("/contact")
	public String show() {
		return "contact";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.POST)
	public String post(Principal principal, @RequestParam(required = false) String email, @RequestParam String question) {
		// TODO send email
		System.out.println("TODO send email");
		return "redirect:/contact.html?sent=true";
	}

}
