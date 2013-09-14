package com.dipgen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@RequestMapping("/tos")
	public String tos() {
		return "tos";
	}

	@RequestMapping("/privacy-policy")
	public String privacyPolicy() {
		return "privacy-policy";
	}
}
