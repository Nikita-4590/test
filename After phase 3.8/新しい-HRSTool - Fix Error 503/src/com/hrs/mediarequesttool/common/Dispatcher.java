package com.hrs.mediarequesttool.common;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.web.servlet.DispatcherServlet;

import com.hrs.mediarequesttool.mail.MailSender;

public class Dispatcher extends DispatcherServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2008579019124175620L;

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		MailSender.init(config.getServletContext());
	}
}
