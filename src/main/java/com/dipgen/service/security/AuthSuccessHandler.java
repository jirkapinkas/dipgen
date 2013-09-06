package com.dipgen.service.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.dipgen.entity.security.User;

@Service("authSuccessHandler")
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		super.onAuthenticationSuccess(request, response, authentication);

		String name = authentication.getName();
		User user = userService.findOne(name);
		user.setInvalidLoginCount(0);
		user.setLastLoginDate(new Date());
		userService.update(user);
	}

}
