package com.dipgen.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.dipgen.entity.security.User;

@Entity
public class Diploma {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "diploma_id")
	private Integer diplomaId;

	@Lob
	private String svg;

	@Lob
	private byte[] thumbnail;

	private String name;

	@OneToMany(mappedBy = "diploma", cascade = CascadeType.REMOVE)
	private List<GeneratorString> generatorString;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<GeneratorString> getGeneratorString() {
		return generatorString;
	}

	public void setGeneratorString(List<GeneratorString> generatorString) {
		this.generatorString = generatorString;
	}

	public byte[] getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Integer getDiplomaId() {
		return diplomaId;
	}

	public void setDiplomaId(Integer diplomaId) {
		this.diplomaId = diplomaId;
	}

	public String getSvg() {
		return svg;
	}

	public void setSvg(String svg) {
		this.svg = svg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
