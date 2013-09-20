package com.dipgen.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DipgenSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "dipgen_settings_id")
	private Integer dipgenSettingsId;

	@Column(name = "email_host")
	private String emailHost;

	@Column(name = "email_port")
	private int emailPort;

	@Column(name = "email_smtp_username")
	private String emailSmtpUsername;

	@Column(name = "email_smtp_password")
	private String emailSmtpPassword;

	@Column(name = "email_central_address")
	private String emailCentralAddress;

	public String getEmailCentralAddress() {
		return emailCentralAddress;
	}

	public void setEmailCentralAddress(String emailCentralAddress) {
		this.emailCentralAddress = emailCentralAddress;
	}

	public Integer getDipgenSettingsId() {
		return dipgenSettingsId;
	}

	public void setDipgenSettingsId(Integer dipgenSettingsId) {
		this.dipgenSettingsId = dipgenSettingsId;
	}

	public String getEmailHost() {
		return emailHost;
	}

	public void setEmailHost(String emailHost) {
		this.emailHost = emailHost;
	}

	public int getEmailPort() {
		return emailPort;
	}

	public void setEmailPort(int emailPort) {
		this.emailPort = emailPort;
	}

	public String getEmailSmtpUsername() {
		return emailSmtpUsername;
	}

	public void setEmailSmtpUsername(String emailSmtpUsername) {
		this.emailSmtpUsername = emailSmtpUsername;
	}

	public String getEmailSmtpPassword() {
		return emailSmtpPassword;
	}

	public void setEmailSmtpPassword(String emailSmtpPassword) {
		this.emailSmtpPassword = emailSmtpPassword;
	}

}
