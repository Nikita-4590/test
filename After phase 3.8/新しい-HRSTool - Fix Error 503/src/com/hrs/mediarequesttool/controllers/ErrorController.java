package com.hrs.mediarequesttool.controllers;

import javax.servlet.http.HttpServletResponse;



import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hrs.mediarequesttool.common.exception.ResourceNotFoundException;

@Controller
@RequestMapping("/err")
public class ErrorController extends BaseController {

	@RequestMapping(value = "/")
	public ModelAndView badRequest(ModelMap model) {
		ExceptionUtils utils = new ExceptionUtils();
		model.put("err_utils", utils);		
		return view("error", model);
	}

	@RequestMapping(value = "/not_found/")
	public ModelAndView notFound(HttpServletResponse response) {
		response.setStatus(404);
		throw new ResourceNotFoundException();
	}
}
