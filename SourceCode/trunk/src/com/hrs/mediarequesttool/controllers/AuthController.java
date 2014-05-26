package com.hrs.mediarequesttool.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController {

	@RequestMapping(value = "/login/")
	public ModelAndView login() {

		ViewBuilder viewBuilder = getViewBuilder("auth.login");
		viewBuilder.setStylesheets("auth.login.css", "global.form.css");
		viewBuilder.setPageTitle("ログイン");

		return view(viewBuilder);
	}

	@RequestMapping("/fail/")
	public ModelAndView fail(HttpServletRequest httpRequest, ModelMap model) {
	
		model.addAttribute("message", httpRequest.getParameter("message"));

		ViewBuilder viewBuilder = getViewBuilder("auth.login", model);
		viewBuilder.setStylesheets("auth.login.css", "global.form.css");
		viewBuilder.setPageTitle("ログイン");
		return view(viewBuilder);
	}
}
