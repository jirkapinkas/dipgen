package com.dipgen.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dipgen.dto.DiplomaJsonDto;
import com.dipgen.entity.Diploma;
import com.dipgen.service.DiplomaJsonService;
import com.dipgen.service.DiplomaService;
import com.dipgen.service.security.UserService;
import com.dipgen.service.util.SvgUtil;

@Controller
public class DiplomaController {

	@Autowired
	private DiplomaService diplomaService;

	@Autowired
	private DiplomaJsonService diplomaJsonService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/diplomas")
	public String showList() {
		return "diploma-list";
	}

	@RequestMapping(value = "/diplomas", params = "image")
	public void thumbnail(Model model, HttpServletResponse response, @RequestParam int image, Principal principal) throws IOException {
		Diploma diploma = diplomaService.findOne(image, principal.getName());
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP
																					// 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
		response.getOutputStream().write(diploma.getThumbnail());
	}

	@RequestMapping(value = "/diplomas", params = "json")
	@ResponseBody
	public List<DiplomaJsonDto> list(Principal principal) {
		return diplomaJsonService.list(principal.getName());
	}

	@RequestMapping("/diplomas/new")
	public String emptyEditor(Model model) {
		model.addAttribute("svgFile", diplomaService.getEmptyDiploma());
		model.addAttribute("diplomaName", "Untitled diploma");
		return "diploma-editor";
	}

	@RequestMapping("/diplomas/edit")
	public String editEditor(Model model, @RequestParam int id, Principal principal) {
		Diploma diploma = diplomaService.findOne(id, principal.getName());
		model.addAttribute("svgFile", SvgUtil.normalizeSvg(diploma.getSvg()));
		model.addAttribute("diplomaName", diploma.getName());
		return "diploma-editor";
	}

	@RequestMapping(value = "/diplomas/edit", method = RequestMethod.POST)
	public String saveEdit(Model model, @RequestParam String svg, @RequestParam(required = false) String name, @RequestParam int id, Principal principal) {
		Diploma diploma = diplomaService.findOne(id, principal.getName());
		diploma.setSvg(svg);
		diploma.setName(name);
		diplomaService.save(diploma);
		return "diploma-editor";
	}

	@RequestMapping(value = "/diplomas/new", method = RequestMethod.POST)
	public String saveNew(Model model, @RequestParam String svg, @RequestParam(required = false) String name, Principal principal) {
		Diploma diploma = new Diploma();
		diploma.setSvg(svg);
		diploma.setName(name);
		diploma.setUser(userService.findOne(principal.getName()));
		diplomaService.save(diploma);
		return "diploma-editor";
	}

	@RequestMapping("/diplomas/delete")
	public String delete(@RequestParam int id, Principal principal) {
		diplomaService.remove(id, principal.getName());
		return "redirect:/diplomas.html";
	}

}
