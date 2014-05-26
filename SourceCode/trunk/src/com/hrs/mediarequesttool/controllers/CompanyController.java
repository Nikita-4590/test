package com.hrs.mediarequesttool.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hrs.mediarequesttool.common.DBConnection;
import com.hrs.mediarequesttool.common.PagingResult;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.dals.CompanyDAL;
import com.hrs.mediarequesttool.dals.DALFactory;
import com.hrs.mediarequesttool.pojos.Company;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {
	
	@RequestMapping("/")
	public ModelAndView index(ModelMap model) {
		return redirect("company/list/", model);
	}

	@RequestMapping(value = "/list/")
	public ModelAndView list(ModelMap model) {
		ViewBuilder viewBuilder = getViewBuilder("company.list", model);
		viewBuilder.setScripts("company.list.js");
		viewBuilder.setStylesheets("company.list.css");
		viewBuilder.setPageTitle("企業一覧");
		return view(viewBuilder);
	}

	@RequestMapping(value = "/ajax_list/", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView ajaxList(HttpServletRequest httpRequest, ModelMap model) {
		try {
			String pageParam = httpRequest.getParameter("page");
			String sortParam = httpRequest.getParameter("sort");
			String directionParam = httpRequest
					.getParameter("sort_direction");
			String queryParam = httpRequest.getParameter("query");

			int page = pageParam == null ? -1 : Integer.parseInt(pageParam);

			SqlSessionFactory sqlSessionFactory = DBConnection
					.getSqlSessionFactory(this.servletContext,
							DBConnection.DATABASE_PADB_PUBLIC, false);

			CompanyDAL companyDAL = DALFactory.getDAL(CompanyDAL.class,
					sqlSessionFactory);

			if (page == -1) {
				List<Company> companies = companyDAL.getAll(sortParam,
						directionParam, queryParam);

				model.addAttribute("fullCompanies", companies);
			} else {
				PagingResult<Company> companies = null;
				companies = companyDAL.paging(page, sortParam, directionParam,
						queryParam);
				model.addAttribute("pagingCompanies", companies);
			}

		} catch (GenericException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return view("company.ajax_list", model);
	}
}
