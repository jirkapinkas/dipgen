package com.dipgen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dipgen.entity.DipgenSettings;

@Service
public class JavaMailSenderService {

	@Autowired
	private SettingsService settingsService;

	@Async
	private void sendEmail(String subject, String body) {
		DipgenSettings settings = settingsService.load();
		JavaMailSenderImpl javamailSender = new JavaMailSenderImpl();
		javamailSender.setHost(settings.getEmailHost());
		javamailSender.setPort(settings.getEmailPort());
		javamailSender.setUsername(settings.getEmailSmtpUsername());
		javamailSender.setPassword(settings.getEmailSmtpPassword());
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setSubject(subject);
		mailMessage.setText(body);
		mailMessage.setTo(settings.getEmailCentralAddress());
		mailMessage.setFrom("noreply@dipgen.com");
		javamailSender.send(mailMessage);
	}

	public void requestPremium(String username) {
		sendEmail("dipgen: request premium", "user " + username + " requested premium.");
	}

	public void contactForm(String email, String username, String question) {
		sendEmail("dipgen: contact form", "user: " + username + "\n" + "email: " + email + "\n\n" + "question: " + question);
	}
}
