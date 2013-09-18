package com.dipgen.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dipgen.entity.GeneratorString;
import com.dipgen.service.GeneratorService;
import com.dipgen.service.ResultGeneratorService;
import com.dipgen.service.security.UserService;

@Controller
public class GeneratorController {

	@Autowired
	private GeneratorService generatorService;

	@Autowired
	private ResultGeneratorService resultGeneratorService;
	
	@Autowired
	private UserService userService;

	@RequestMapping("/diplomas/generate")
	public String showGenerator(@RequestParam int id, Model model, Principal principal) {
		model.addAttribute("generatorStrings", generatorService.getGeneratorStrings(id, principal.getName()));
		model.addAttribute("isPremium", userService.isPremium(principal.getName()));
		return "generator";
	}

	public static class GenerateException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public GenerateException(Exception ex) {
			super(ex);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/diplomas/generate", method = RequestMethod.POST)
	public void generateDiploma(@RequestParam int id, @RequestParam boolean singlePdf, HttpServletRequest request, HttpServletResponse response, Principal principal) throws IOException {
		if (singlePdf) {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"diplomas.pdf\"");
		} else {
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=\"diplomas.zip\"");
		}
		FileInputStream inputStream = null;
		OutputStream outputStream = null;
		File generatedFile = null;
		try {
			generatedFile = resultGeneratorService.generate(id, request.getParameterMap(), singlePdf, principal.getName());
			inputStream = FileUtils.openInputStream(generatedFile);
			outputStream = response.getOutputStream();
			IOUtils.copy(inputStream, outputStream);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new GenerateException(ex);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (generatedFile != null) {
				if (!generatedFile.delete()) {
					System.err.println("could not delete temporary file");
				}
			}
		}
	}

	@RequestMapping("/generator/toggle-enabled")
	public String toggleEnabled(@RequestParam int id, Principal principal) {
		generatorService.toggleEnabled(id, principal.getName());
		return "generator";
	}

	@RequestMapping("/generator/toggle-type")
	public String toggleType(@RequestParam int id, @RequestParam GeneratorString.HtmlComponentType type, Principal principal) {
		generatorService.changeType(id, type, principal.getName());
		return "generator";
	}

}
