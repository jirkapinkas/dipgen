package com.dipgen.service;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dipgen.entity.Diploma;
import com.dipgen.entity.GeneratorString;
import com.dipgen.entity.GeneratorString.HtmlComponentType;
import com.dipgen.repository.DiplomaRepository;
import com.dipgen.repository.GeneratorStringRepository;
import com.dipgen.service.security.InvalidUserException;
import com.dipgen.service.util.SvgUtil;

@Service
@Transactional
public class GeneratorService {

	@Autowired
	private DiplomaRepository diplomaRepository;

	@Autowired
	private GeneratorStringRepository generatorStringRepository;

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Get generator Strings for diploma. Diploma could have changed, so this
	 * method compares current diploma with database and merges them together
	 * (and saves the result to database for future use).
	 * 
	 * @param diplomaId
	 *            Diploma ID
	 * @return
	 */
	public List<GeneratorString> getGeneratorStrings(int diplomaId, String username) {
		Diploma diploma = diplomaRepository.findByDiplomaIdAndUserName(diplomaId, username);
		if(diploma == null) {
			throw new InvalidUserException("Invalid user!");
		}
		// create map of current diploma strings
		List<String> diplomaStrings = SvgUtil.parseTexts(diploma.getSvg());
		HashMap<String, String> diplomaStringsMap = new HashMap<String, String>();
		for (String string : diplomaStrings) {
			diplomaStringsMap.put(string, string);
		}

		// create map of all diploma strings in database
		List<GeneratorString> generatorStrings = diploma.getGeneratorString();
		HashMap<String, GeneratorString> generatorStringsMap = new HashMap<String, GeneratorString>();
		for (GeneratorString generatorString : generatorStrings) {
			generatorStringsMap.put(generatorString.getString(), generatorString);
		}

		// merge two maps together
		for (String string : diplomaStrings) {
			if (!generatorStringsMap.containsKey(string)) {
				GeneratorString generatorString = new GeneratorString();
				generatorString.setDiploma(diploma);
				generatorString.setString(string);
				generatorStringRepository.save(generatorString);
			}
		}
		for (GeneratorString generatorString : generatorStrings) {
			if (!diplomaStringsMap.containsKey(generatorString.getString())) {
				generatorStringRepository.delete(generatorString);
			}
		}
		generatorStringRepository.flush();
		// TODO make this more clear - it would be great to just simply refresh
		// diploma entity and not use EntityManager at all
		return entityManager.createQuery("select gs from GeneratorString gs where gs.diploma.diplomaId = :diplomaId", GeneratorString.class).setParameter("diplomaId", diplomaId).getResultList();
	}

	public void toggleEnabled(int id, String username) {
		GeneratorString generatorString = generatorStringRepository.findOne(id);
		if(! generatorString.getDiploma().getUser().getName().equals(username)) {
			throw new InvalidUserException();
		}
		generatorString.setEnabled(!generatorString.isEnabled());
	}

	public void changeType(int id, GeneratorString.HtmlComponentType type, String username) {
		GeneratorString generatorString = generatorStringRepository.findOne(id);
		if(! generatorString.getDiploma().getUser().getName().equals(username)) {
			throw new InvalidUserException();
		}
		generatorString.setHtmlComponentType(type);
	}

	public GeneratorString find(int id, String username) {
		GeneratorString generatorString = generatorStringRepository.findOne(id);
		if(! generatorString.getDiploma().getUser().getName().equals(username)) {
			throw new InvalidUserException();
		}
		return generatorString;
	}

	public GeneratorString save(GeneratorString generatorString) {
		return generatorStringRepository.save(generatorString);
	}

	public List<GeneratorString> findEnabledTextfields(int diplomaId) {
		return entityManager
				.createQuery("select gs from GeneratorString gs where gs.diploma.diplomaId = :diplomaId and gs.enabled = true and gs.htmlComponentType = :htmlComponentType", GeneratorString.class)
				.setParameter("diplomaId", diplomaId).setParameter("htmlComponentType", HtmlComponentType.TEXTFIELD).getResultList();
	}

	public GeneratorString findEnabledTextarea(int diplomaId) {
		List<GeneratorString> resultList = entityManager
				.createQuery("select gs from GeneratorString gs where gs.diploma.diplomaId = :diplomaId and gs.enabled = true and gs.htmlComponentType = :htmlComponentType", GeneratorString.class)
				.setParameter("diplomaId", diplomaId).setParameter("htmlComponentType", HtmlComponentType.TEXTAREA).getResultList();
		if (resultList.size() == 1) {
			return resultList.get(0);
		} else if (resultList.size() == 0) {
			return null;
		} else {
			throw new NonUniqueResultException("you must have max. single textarea!");
		}
	}

}
