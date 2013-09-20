package com.dipgen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dipgen.entity.DipgenSettings;
import com.dipgen.repository.SettingsRepository;

@Service
public class SettingsService {

	@Autowired
	private SettingsRepository settingsRepository;

	public DipgenSettings load() {
		List<DipgenSettings> settingsList = settingsRepository.findAll();
		if (settingsList.size() == 0) {
			return new DipgenSettings();
		} else {
			return settingsList.get(0);
		}
	}

	public void save(DipgenSettings settings) {
		if (settings.getDipgenSettingsId() == null || settings.getDipgenSettingsId() == 0) {
			settings.setDipgenSettingsId(load().getDipgenSettingsId());
		}
		settingsRepository.save(settings);
	}
}
