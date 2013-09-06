package com.dipgen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dipgen.entity.Diploma;
import com.dipgen.repository.DiplomaRepository;

@Service
@Transactional
public class DiplomaService {

	@Autowired
	private DiplomaRepository diplomaRepository;

	public Diploma findOne(int id, String username) {
		return diplomaRepository.findByDiplomaIdAndUserName(id, username);
	}

	public List<Diploma> listAll(String username) {
		return diplomaRepository.findByUserName(username);
	}

	public String getEmptyDiploma() {
		return "<svg xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:se=\"http://svg-edit.googlecode.com\" xmlns=\"http://www.w3.org/2000/svg\" overflow=\"visible\" y=\"480\" x=\"640\" height=\"480\" width=\"640\" id=\"svgcontent\"></svg>";
	}

	public Diploma save(Diploma diploma) {
		byte[] jpegThumbnail = DiplomaUtil.generateJpegThumbnail(diploma.getSvg(), DiplomaUtil.THUMBNAIL_HEIGHT_SMALL);
		diploma.setThumbnail(jpegThumbnail);
		return diplomaRepository.save(diploma);
	}

	public void remove(int id, String username) {
		diplomaRepository.deleteWithDiplomaIdAndUserName(id, username);
	}

}
