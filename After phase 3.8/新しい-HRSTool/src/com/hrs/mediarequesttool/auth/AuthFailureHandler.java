package com.hrs.mediarequesttool.auth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AuthFailureHandler implements AuthenticationFailureHandler {

	private Logger logger = Logger.getLogger(AuthFailureHandler.class);

	@SuppressWarnings("deprecation")
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

		logger.info("[" + exception.getAuthentication().getPrincipal() + "] : LOGIN FAILED from " + request.getRemoteAddr());

		// Get message ID
		String messageID = exception.getMessage();

		response.sendRedirect(request.getContextPath() + "/auth/fail/?message=" + messageID);
	}

}
