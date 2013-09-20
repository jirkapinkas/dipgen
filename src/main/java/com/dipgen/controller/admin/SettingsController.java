package com.dipgen.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dipgen.entity.DipgenSettings;
import com.dipgen.service.SettingsService;

@Controller
@RequestMapping("/admin/settings")
public class SettingsController {

	@Autowired
	private SettingsService settingsService;

	@RequestMapping
	public String show(Model model) {
		DipgenSettings settings = settingsService.load();
		model.addAttribute("dipgenSettings", settings);
		return "admin/settings";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String save(@ModelAttribute DipgenSettings dipgenSettings) {
		settingsService.save(dipgenSettings);
		return "redirect:/admin/settings.html?success=true";
	}
}
