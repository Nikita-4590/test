package com.hrs.mediarequesttool.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

public class AjaxAwareAuthenticationEntryPoint extends
		Http403ForbiddenEntryPoint {

	Logger logger = Logger.getLogger(AjaxAwareAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		String ajax = request.getParameter("ajax");

		if (ajax == null) {
			response.sendRedirect(request.getContextPath() + "/auth/login/");
		} else {
			super.commence(request, response, authException);
		}
	}
}
