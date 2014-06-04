package com.hrs.mediarequesttool.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.hrs.mediarequesttool.common.exception.BadRequestException;
import com.hrs.mediarequesttool.common.Constants;
import com.hrs.mediarequesttool.common.DBConnection;
import com.hrs.mediarequesttool.common.PagingResult;
import com.hrs.mediarequesttool.common.Role;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.common.exception.ResourceNotFoundException;
import com.hrs.mediarequesttool.common.validator.Validator;
import com.hrs.mediarequesttool.dals.CommentDAL;
import com.hrs.mediarequesttool.dals.DALFactory;
import com.hrs.mediarequesttool.dals.MediaLabelDAL;
import com.hrs.mediarequesttool.dals.RelationRequestDAL;
import com.hrs.mediarequesttool.dals.StatusDAL;
import com.hrs.mediarequesttool.pojos.MediaLabel;
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
		viewBuilder.setPageTitle("依頼一覧");
		return view(viewBuilder);
	}

	@RequestMapping(value = "/ajax_list/", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView ajaxList(HttpServletRequest httpRequest, ModelMap model, Authentication authentication) throws UnsupportedEncodingException {
		try {
			String pageParam = httpRequest.getParameter("page");
			String sortParam = httpRequest.getParameter("sort");
			String requestIdParam = httpRequest.getParameter("id");
			String statusParam = httpRequest.getParameter("status");
			String directionParam = httpRequest.getParameter("sort_direction");
			String companyParam = httpRequest.getParameter("company_id");
			String mediaParam = httpRequest.getParameter("media_id");
			int page = pageParam == null ? -1 : Integer.parseInt(pageParam);
			
			RelationRequestDAL requestDAL = getDAL(RelationRequestDAL.class);
			Role role = new Role(authentication.getPrincipal());
			PagingResult<RelationRequest> relationRequests = requestDAL.paging(page, directionParam, sortParam, requestIdParam, statusParam, companyParam, mediaParam,
					role.getRoles(),role.getPriority());
			model.addAttribute("relationRequests", relationRequests);
			model.addAttribute("compare_status", Constants.HIGHT_LIGHT_RECORD);

		} catch (GenericException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view("request.ajax_list", model);
	}

	@RequestMapping(value = "/load_status", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String loadStatus(Authentication authentication, HttpServletResponse response) throws GenericException {
		String result = null;
		try {
			StatusDAL statusDAL = getDAL(StatusDAL.class);
			Role role = new Role(authentication.getPrincipal());			
			List<Status> status = statusDAL.getAll(role.getRoles());
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

	@RequestMapping("/view_request/{relation_request_id}/")
	public ModelAndView viewRequest(HttpServletRequest httpRequest, @PathVariable("relation_request_id") int requestId, ModelMap model, RedirectAttributes redirectAttributes) {
		ViewBuilder builder = getViewBuilder("request.detail", model);

		try {
			// get data from database
			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail

			RelationRequestDAL requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);

			if (request == null) {
				throw new ResourceNotFoundException();
			}

			StatusDAL statusDAL = DALFactory.getDAL(StatusDAL.class, sqlSessionFactory);
			String[] listNextStatus = null;

			if (StringUtils.strip(request.getStatus()).equals("NEW")) {
				listNextStatus = Constants.NEXT_NEW;
			} else if (StringUtils.strip(request.getStatus()).equals("ASSIGNED")) {
				listNextStatus = Constants.NEXT_ASSIGNED;
			} else if (StringUtils.strip(request.getStatus()).equals("CONFIRMING")) {
				listNextStatus = Constants.NEXT_CONFIRMING;
			} else if (StringUtils.strip(request.getStatus()).equals("OK")) {
				listNextStatus = Constants.NEXT_OK;
			} else if (StringUtils.strip(request.getStatus()).equals("NG")) {
				listNextStatus = Constants.NEXT_NG;
			} else if (StringUtils.strip(request.getStatus()).equals("FINISHED")) {
				listNextStatus = Constants.NEXT_FINISHED;
			} else if (StringUtils.strip(request.getStatus()).equals("DELETED")) {
				listNextStatus = Constants.NEXT_DELETED;
			}

			List<Status> listStatus = statusDAL.getListNextStatus(listNextStatus);
			
			MediaLabelDAL mediaLabelDAL = DALFactory.getDAL(MediaLabelDAL.class, sqlSessionFactory);
			
			MediaLabel mediaLabel = mediaLabelDAL.get(request.getMedia_id());
			
			if (mediaLabel == null) {
				throw new ResourceNotFoundException();
			} 
			
			model.addAttribute("listStatus", listStatus);
			model.addAttribute("request", request);
			model.addAttribute("mediaLabel", mediaLabel);

			builder.setPageTitle("依頼詳細");
			builder.setStylesheets("global.form.css", "request.detail.css");
			builder.setScripts("jquery/jquery.form.min.js", "request.detail.js", "request.change.js", "request.renkei.js");

		} catch (Exception e) {
			// TODO: handle exception
		}

		return view(builder);
	}

	@RequestMapping(value = "/confirm_change/", method = RequestMethod.POST)
	public ModelAndView confirmChange(HttpServletRequest httpRequest, ModelMap model) throws RuntimeException {
		try {
			ViewBuilder builder = getViewBuilder("request.confirm-change", model);
			int requestId = Integer.parseInt(httpRequest.getParameter("relation_request_id"));

			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			RelationRequestDAL requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);

			if (request == null) {
				throw new ResourceNotFoundException();
			}

			String newAssignedPerson = httpRequest.getParameter("assign_user_name");
			String newStatus = httpRequest.getParameter("new_status");
			boolean isValidData = false;

			isValidData = validateAssignerAndStatus(newAssignedPerson, request.getStatus(), newStatus);

			if (!isValidData) {
				throw new ResourceNotFoundException();
			}

			StatusDAL statusDAL = DALFactory.getDAL(StatusDAL.class, sqlSessionFactory);

			Status changedStatus = statusDAL.get(newStatus);

			model.addAttribute("request", request);
			model.addAttribute("newAssignedPerson", newAssignedPerson);
			model.addAttribute("changedStatus", changedStatus);

			return view(builder);
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException(e, this.getClass());
		} catch (GenericException e) {
			throw new BadRequestException(e, this.getClass());
		}
	}
	
	@RequestMapping(value = "/confirm_change_renkei_date/", method = RequestMethod.POST)
	public ModelAndView confirmChangeRenkeiDate(HttpServletRequest httpRequest, ModelMap model) throws RuntimeException {
		try {
			ViewBuilder builder = getViewBuilder("request.confirm-change-renkei-date", model);
			int requestId = Integer.parseInt(httpRequest.getParameter("relation_request_id"));
			String renkeiDate = httpRequest.getParameter("renkei_date");

			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			RelationRequestDAL requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);

			if (request == null || !validateRenkeiDate(renkeiDate)) {
				throw new ResourceNotFoundException();
			}

			model.addAttribute("request", request);
			model.addAttribute("renkeiDate", renkeiDate);

			return view(builder);
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException(e, this.getClass());
		} catch (GenericException e) {
			throw new BadRequestException(e, this.getClass());
		}
	}

	@ResponseBody
	@RequestMapping(value = "/change/", method = RequestMethod.POST)
	public String submitChange(HttpServletRequest httpRequest, ModelMap model, RedirectAttributes redirectAttributes) throws ResourceNotFoundException {

		String messageId = null;
		boolean success = false;
		SqlSession session = null;
		RelationRequestDAL requestDAL = null;
		CommentDAL commentDAL = null;

		try {
			int requestId = Integer.parseInt(httpRequest.getParameter("relation_request_id"));

			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);

			if (request == null) {
				// inform error message about invalid data
				messageId = "WRN2";
			} else {
				
				String newAssignedPerson = httpRequest.getParameter("assign_user_name");
				String newStatus = httpRequest.getParameter("new_status");

				boolean isValidData = validateAssignerAndStatus(newAssignedPerson, request.getStatus(), newStatus);

				if (isValidData) {
					request.setStatus(newStatus);
					request.setAssign_user_name(newAssignedPerson);
					
					// create session
					session = sqlSessionFactory.openSession();

					requestDAL.setSession(session);
					
					// get old information before update
					RelationRequest oldRequest = requestDAL.get(request.getRelation_request_id());
					
					// update request
					requestDAL.updateRequest(request);
					
					// get new information after update
					
					RelationRequest newRequest = requestDAL.get(request.getRelation_request_id());
					
					// open sql session
					commentDAL = DALFactory.getDAL(CommentDAL.class, sqlSessionFactory);
					commentDAL.setSession(session);
					
					// Insert into table comment
					commentDAL.updateRequest(oldRequest, newRequest);
					
					session.commit();
					
					// Display information message when delete successful
					messageId = "INF1";
					success = true;
				} else {
					// inform error message about invalid data
					messageId = "WRN2";
				}
			}

		} catch (GenericException e) {
			// inform error message about invalid data
			messageId = "WRN2";
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			if (requestDAL != null) {
				requestDAL.forceCloseSession();
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message_id", messageId);
		map.put("success", success);

		if (success) {
			map.put("url", "/request/list/");
		}

		return GSON.toJson(map);
	}

	private boolean validateAssignerAndStatus(String assignedPerson, String currentStatus, String newStatus) {
		if (Validator.isNullOrEmpty(assignedPerson) || Validator.checkExceedLength(Constants.MAX_LENGTH_ASSIGNED_PERSON, assignedPerson)) {
			return false;
		}

		if (StringUtils.strip(currentStatus).equals("NEW")) {
			if (!Arrays.asList(Constants.NEXT_NEW).contains(StringUtils.strip(newStatus))) {
				return false;
			}
		} else if (StringUtils.strip(currentStatus).equals("ASSIGNED")) {
			if (!Arrays.asList(Constants.NEXT_ASSIGNED).contains(StringUtils.strip(newStatus))) {
				return false;
			}
		} else if (StringUtils.strip(currentStatus).equals("CONFIRMING")) {
			if (!Arrays.asList(Constants.NEXT_CONFIRMING).contains(StringUtils.strip(newStatus))) {
				return false;
			}
		} else if (StringUtils.strip(currentStatus).equals("OK")) {
			if (!Arrays.asList(Constants.NEXT_OK).contains(StringUtils.strip(newStatus))) {
				return false;
			}
		} else if (StringUtils.strip(currentStatus).equals("NG")) {
			if (!Arrays.asList(Constants.NEXT_NG).contains(StringUtils.strip(newStatus))) {
				return false;
			}
		} else if (StringUtils.strip(currentStatus).equals("FINISHED")) {
			if (!Arrays.asList(Constants.NEXT_FINISHED).contains(StringUtils.strip(newStatus))) {
				return false;
			}
		} else if (StringUtils.strip(currentStatus).equals("DELETED")) {
			if (!Arrays.asList(Constants.NEXT_DELETED).contains(StringUtils.strip(newStatus))) {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}
	
	private boolean validateRenkeiDate(String renkeiDate) {
		if (Validator.isNullOrEmpty(renkeiDate)) {
			return false;
		} else {
			DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(Constants.DATE_FORMAT);
			
			DateTime crawlDate = DateTime.parse(renkeiDate, dateFormatter);
			
			DateTime tomorrow = DateTime.now().plusDays(1).withTime(0, 0, 0, 0);
			
			boolean checkCrawlDate = crawlDate.isAfter(tomorrow) || crawlDate.isEqual(tomorrow);
			
			return checkCrawlDate;
		}
	}

}
