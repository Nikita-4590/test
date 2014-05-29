package com.hrs.mediarequesttool.controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.hrs.mediarequesttool.common.PagingResult;
import com.hrs.mediarequesttool.common.Role;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.dals.RelationRequestDAL;
import com.hrs.mediarequesttool.dals.StatusDAL;
import com.hrs.mediarequesttool.pojos.RelationRequest;
import com.hrs.mediarequesttool.pojos.Status;

@Controller
@RequestMapping("/request")
public class RelationRequestController extends BaseController {

	@RequestMapping("/")
	public ModelAndView index(ModelMap model) {
		return redirect("request/list/", model);
	}

	@RequestMapping("/list/")
	public ModelAndView getAllRequest(ModelMap model) throws GenericException {
		ViewBuilder viewBuilder = getViewBuilder("request.list", model);
		viewBuilder.setScripts("request.list.js");
		viewBuilder.setStylesheets("relation.list.css", "global.css");
		viewBuilder.setPageTitle("List");
		return view(viewBuilder);
	}

	@RequestMapping(value = "/ajax_list/", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView ajaxList(HttpServletRequest httpRequest,
			ModelMap model, Authentication authentication) throws UnsupportedEncodingException {
		String pageParam = httpRequest.getParameter("page");
		String sortParam = httpRequest.getParameter("sort");
		String requestIdParam = httpRequest.getParameter("id");
		String statusParam = httpRequest.getParameter("status");
		String directionParam = httpRequest.getParameter("sort_direction");
		String companyParam = httpRequest.getParameter("company_id");
		String mediaParam = httpRequest.getParameter("media_id");
		int page = pageParam == null ? -1 : Integer.parseInt(pageParam);
		try {

			RelationRequestDAL requestDAL = getDAL(RelationRequestDAL.class);
			Role role = new Role();
			PagingResult<RelationRequest> relationRequests = requestDAL.paging(
					page, directionParam, sortParam, requestIdParam,
					statusParam, companyParam, mediaParam,
					role.generateSQL(authentication.getPrincipal()));
			model.addAttribute("relationRequests", relationRequests);
			model.addAttribute("compare_status", "担当者決定");

		} catch (GenericException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view("request.ajax_list", model);
	}

	@RequestMapping(value = "/load_status", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String loadStatus(Authentication authentication,
			HttpServletResponse response) throws GenericException {
		String result = null;
		try {
			StatusDAL statusDAL = getDAL(StatusDAL.class);
			Role role = new Role();
			List<Status> status = statusDAL.getAll(role
					.generateSQL(authentication.getPrincipal()));
			Gson gson = new Gson();
			result = gson.toJson(status);
			return result;
		} catch (GenericException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
