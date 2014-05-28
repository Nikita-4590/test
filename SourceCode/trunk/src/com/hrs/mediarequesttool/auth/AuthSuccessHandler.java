package com.hrs.mediarequesttool.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.hrs.mediarequesttool.pojos.User;

public class AuthSuccessHandler implements AuthenticationSuccessHandler {

	private Logger logger = Logger.getLogger(AuthSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		User user = AuthProvider.getUser();

		logger.info("[" + user.getUser_id() + "] : LOGIN SUCCEEDED from " + request.getRemoteAddr());

		response.sendRedirect(request.getContextPath() + "/request/");
	}

}
