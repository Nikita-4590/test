package com.hrs.mediarequesttool.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
import com.hrs.mediarequesttool.dals.UserDAL;
import com.hrs.mediarequesttool.pojos.MediaLabel;
import com.hrs.mediarequesttool.pojos.RelationRequest;
import com.hrs.mediarequesttool.pojos.Status;
import com.hrs.mediarequesttool.pojos.User;

@Controller
@RequestMapping("/request")
public class RelationRequestController extends BaseController {

	@RequestMapping("/")
	public ModelAndView index(ModelMap model) {
		return redirect("request/list/", model);
	}

	@RequestMapping("/list/")
	public ModelAndView getAllRequest(ModelMap model, Authentication authentication) throws GenericException {
		ViewBuilder viewBuilder = null;
		try {
			StatusDAL statusDAL = getDAL(StatusDAL.class);
			Role role = new Role(authentication.getPrincipal());
			List<Status> lstStatus = statusDAL.getAll(role.getNoneStatus());
			model.addAttribute("lstStatus", lstStatus);
			viewBuilder = getViewBuilder("request.list", model);
			viewBuilder.setScripts("request.list.js");
			viewBuilder.setStylesheets("relation.list.css", "global.css");
			viewBuilder.setPageTitle("依頼一覧");
		} catch(GenericException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return view(viewBuilder);
	}

	@RequestMapping(value = "/ajax_list/", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView ajaxList(HttpServletRequest httpRequest, ModelMap model, Authentication authentication) {
		try {
			String pageParam = httpRequest.getParameter("page");
			String sortParam = httpRequest.getParameter("sort");
			String searchParam = httpRequest.getParameter("id");
			String statusParam = httpRequest.getParameter("status");
			String directionParam = httpRequest.getParameter("sort_direction");
			int page = pageParam == null ? -1 : Integer.parseInt(pageParam);
			RelationRequestDAL requestDAL = getDAL(RelationRequestDAL.class);
			Role role = new Role(authentication.getPrincipal());
			PagingResult<RelationRequest> relationRequests = null;
			if(statusParam != null || searchParam != null) {
				relationRequests = requestDAL.getAllRecord(page, directionParam, sortParam, searchParam, statusParam, 
						role.getPriority(), role.getNoneStatus());
			} else {
				relationRequests = requestDAL.paging(page, directionParam, sortParam,  role.getRoles(), role.getPriority());
			}
			
			model.addAttribute("relationRequests", relationRequests);
			model.addAttribute("compare_status", role.getHightLight());
		} catch (GenericException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view("request.ajax_list", model);
	}

	@RequestMapping("/view_request/{relation_request_id}/")
	public ModelAndView viewRequest(HttpServletRequest httpRequest, @PathVariable("relation_request_id") int requestId, ModelMap model,
			RedirectAttributes redirectAttributes) {
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
			Status nextStatus = new Status();
			String[] listStatus = null;
			List<Status> listNextStatus = new ArrayList<Status>();
			
			if (StringUtils.strip(request.getStatus()).equals("NEW")) {
				
				model.addAttribute("view", "NEW");
				
				nextStatus = statusDAL.get("CONFIRMING");
				
			} else if (StringUtils.strip(request.getStatus()).equals("CONFIRMING") || StringUtils.strip(request.getStatus()).equals("NG")) {
				
				if (StringUtils.strip(request.getStatus()).equals("CONFIRMING")) {
					model.addAttribute("view", "CONFIRMING");
					listStatus = Constants.NEXT_CONFIRMING;
				} else {
					model.addAttribute("view", "NG");
					listStatus = Constants.NEXT_NG;
				}
				listNextStatus = statusDAL.getListNextStatus(listStatus);
				
				model.addAttribute("listNextStatus", listNextStatus);
				
			} else if (StringUtils.strip(request.getStatus()).equals("OK") || StringUtils.strip(request.getStatus()).equals("PROCESSING")) {
				
				if (StringUtils.strip(request.getStatus()).equals("OK")) {
					model.addAttribute("view", "OK");
					nextStatus = statusDAL.get("PROCESSING");
				} else {
					model.addAttribute("view", "PROCESSING");
					nextStatus = statusDAL.get("FINISHED");
				}
				
				UserDAL userDAL = DALFactory.getDAL(UserDAL.class, sqlSessionFactory);
				List<User> directors = userDAL.getListDirector();
				model.addAttribute("directors", directors);
				
			} else if (StringUtils.strip(request.getStatus()).equals("FINISHED")) {
				model.addAttribute("view", "FINISHED");
			} else {
				model.addAttribute("view", "DEFAULT");
			}
			
			if (nextStatus != null) {
				model.addAttribute("nextStatus", nextStatus);
			}

			MediaLabelDAL mediaLabelDAL = DALFactory.getDAL(MediaLabelDAL.class, sqlSessionFactory);

			MediaLabel mediaLabel = mediaLabelDAL.get(request.getMedia_id());

			if (mediaLabel == null) {
				throw new ResourceNotFoundException();
			}
			
			model.addAttribute("request", request);
			model.addAttribute("mediaLabel", mediaLabel);

			builder.setPageTitle("依頼詳細");
			builder.setStylesheets("global.form.css", "request.detail.css");
			builder.setScripts("jquery/jquery.form.min.js", "request.detail.js", "request.change.js", "request.renkei.js");

		} catch (GenericException e) {
			e.printStackTrace();
			return redirect("err/"); // TODO: need check again
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
/*
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
*/
		return true;
	}
}
