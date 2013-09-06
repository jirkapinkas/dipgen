package com.dipgen.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "generator_string")
public class GeneratorString {

	public enum HtmlComponentType {
		UNDEFINED, TEXTFIELD, TEXTAREA
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "generator_id")
	private int generatorId;

	@ManyToOne
	@JoinColumn(name = "diploma_id")
	private Diploma diploma;

	private String string;

	private boolean enabled;

	private HtmlComponentType htmlComponentType;

	public GeneratorString() {
		enabled = true;
		htmlComponentType = HtmlComponentType.UNDEFINED;
	}

	public HtmlComponentType getHtmlComponentType() {
		return htmlComponentType;
	}

	public void setHtmlComponentType(HtmlComponentType htmlComponentType) {
		this.htmlComponentType = htmlComponentType;
	}

	public int getGeneratorId() {
		return generatorId;
	}

	public void setGeneratorId(int generatorId) {
		this.generatorId = generatorId;
	}

	public Diploma getDiploma() {
		return diploma;
	}

	public void setDiploma(Diploma diploma) {
		this.diploma = diploma;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
