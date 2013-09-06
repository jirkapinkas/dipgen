package com.dipgen.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import com.dipgen.entity.security.User;

@Service("authFailureHandler")
public class AuthFailureHandler implements AuthenticationFailureHandler {

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String name = request.getParameter("j_username");
		User user = userService.findOne(name);
		if (user != null) {
			user.setInvalidLoginCount(user.getInvalidLoginCount() + 1);
			userService.update(user);
		}
		response.sendRedirect(request.getContextPath());
	}

}
