package com.dipgen.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dipgen.dto.DiplomaJsonDto;
import com.dipgen.entity.Diploma;
import com.dipgen.repository.DiplomaRepository;

@Service
public class DiplomaJsonService {

	@Autowired
	private DiplomaRepository diplomaRepository;

	public List<DiplomaJsonDto> list(String username) {
		ArrayList<DiplomaJsonDto> result = new ArrayList<DiplomaJsonDto>();
		List<Diploma> list = diplomaRepository.findByUserName(username);
		for (Diploma diploma : list) {
			DiplomaJsonDto dto = new DiplomaJsonDto();
			dto.setDiplomaId(diploma.getDiplomaId());
			dto.setName(diploma.getName());
			result.add(dto);
		}
		return result;
	}

}
