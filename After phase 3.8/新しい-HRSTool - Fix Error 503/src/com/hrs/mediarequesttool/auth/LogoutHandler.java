package com.hrs.mediarequesttool.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.hrs.mediarequesttool.pojos.User;

public class LogoutHandler implements LogoutSuccessHandler {

	private Logger logger = Logger.getLogger(LogoutHandler.class);

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		if (authentication != null && (authentication instanceof UsernamePasswordAuthenticationToken)) {

			Object principal = authentication.getPrincipal();

			if (principal instanceof User) {
				User user = User.class.cast(principal);
				logger.info("[" + user.getUser_id() + "] : LOGOUT from " + request.getRemoteAddr());
			}
		}

		response.sendRedirect(request.getContextPath() + "/");
	}

}
