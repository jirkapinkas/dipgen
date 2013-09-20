package com.dipgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dipgen.entity.DipgenSettings;

public interface SettingsRepository extends JpaRepository<DipgenSettings, Integer>{

}
