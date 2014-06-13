package com.hrs.mediarequesttool.auth;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.ibatis.session.SqlSessionFactory;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.hrs.mediarequesttool.common.DBConnection;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.dals.DALFactory;
import com.hrs.mediarequesttool.dals.UserDAL;
import com.hrs.mediarequesttool.pojos.User;

@Component
public class AuthProvider implements AuthenticationProvider {

	private ServletContext servletContext;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// Get user, password from screen
		try {
			String userID = authentication.getName();
			String userPwd = authentication.getCredentials().toString();

			// Create SQL connection
			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			UserDAL userDAL = DALFactory.getDAL(UserDAL.class, sqlSessionFactory);

			// Get user, password to check authenticator
			User user = userDAL.getbyUserID(userID);

			// In case user doesn't exist
			if (user == null) {
				throw new BadCredentialsException("ERR050");

				// In case password doesn't match
			} else if (!BCrypt.checkpw(userPwd, user.getUser_password())) {
				// update login_failed_count
				userDAL.countLoginFailure(user.getId());
				throw new BadCredentialsException("ERR050");

				// In case user was disabled
			} else if (!user.isEnabled_flag()) {
				throw new BadCredentialsException("ERR051");

				// In case user and password matched
			} else {
				// update last login_at
				userDAL.updateLastLoginTime(user.getId());

				List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
				grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
				Authentication auth = new UsernamePasswordAuthenticationToken(user, null, grantedAuths);
				return auth;
			}

		} catch (GenericException e) {
			// Connect to DB failed
			throw new BadCredentialsException("ERR050");

		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException("ERR050");

		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public static User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && (authentication instanceof UsernamePasswordAuthenticationToken)) {

			Object principal = authentication.getPrincipal();
			Object authenDetail = authentication.getDetails();

			if (principal instanceof User) {
				return User.class.cast(principal);
			}
			if (authenDetail != null && authenDetail instanceof User) {
				return User.class.cast(authenDetail);
			}
		}

		return null;
	}
}
