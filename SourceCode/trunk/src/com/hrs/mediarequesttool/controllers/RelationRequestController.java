package com.hrs.mediarequesttool.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.hrs.mediarequesttool.pojos.RequestChangeInfo;
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
		} catch (GenericException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
			if (statusParam != null || searchParam != null) {
				relationRequests = requestDAL.getAllRecord(page, directionParam, sortParam, searchParam, statusParam, role.getPriority(), role.getNoneStatus());
			} else {
				relationRequests = requestDAL.paging(page, directionParam, sortParam, role.getRoles(), role.getPriority());
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
			Status nextStatus = new Status();
			String[] listStatus = null;
			String currentStatus = request.getStatus();

			List<Status> listNextStatus = new ArrayList<Status>();

			if (currentStatus.equals(Constants.STATUS_NEW)) {

				model.addAttribute("view", Constants.STATUS_NEW);

				nextStatus = statusDAL.get(Constants.STATUS_CONFIRMING);

			} else if (currentStatus.equals(Constants.STATUS_CONFIRMING) || currentStatus.equals(Constants.STATUS_NG)) {

				if (currentStatus.equals(Constants.STATUS_CONFIRMING)) {
					model.addAttribute("view", Constants.STATUS_CONFIRMING);
					listStatus = Constants.NEXT_CONFIRMING;
				} else {
					model.addAttribute("view", Constants.STATUS_NG);
					listStatus = Constants.NEXT_NG;
				}
				listNextStatus = statusDAL.getListNextStatus(listStatus);

				model.addAttribute("listNextStatus", listNextStatus);

			} else if (currentStatus.equals(Constants.STATUS_OK) || currentStatus.equals(Constants.STATUS_PROCESSING)) {

				if (currentStatus.equals(Constants.STATUS_OK)) {
					model.addAttribute("view", Constants.STATUS_OK);
					nextStatus = statusDAL.get(Constants.STATUS_PROCESSING);
				} else {
					model.addAttribute("view", Constants.STATUS_PROCESSING);
					nextStatus = statusDAL.get(Constants.STATUS_FINISHED);
				}

				UserDAL userDAL = DALFactory.getDAL(UserDAL.class, sqlSessionFactory);
				List<User> directors = userDAL.getListDirector(request.getAssign_user_id());
				model.addAttribute("directors", directors);

			} else if (currentStatus.equals(Constants.STATUS_FINISHED)) {
				model.addAttribute("view", Constants.STATUS_FINISHED);
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
			builder.setScripts("jquery/jquery.form.min.js", "request.detail.js", "request.change.js", "request.update.director.js");

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

			String currentStatus = request.getStatus();

			if (currentStatus.equals(Constants.STATUS_CONFIRMING) || currentStatus.equals(Constants.STATUS_NG)) {
				String nextStatus = httpRequest.getParameter("selected_next_status");

				if (currentStatus.equals(Constants.STATUS_CONFIRMING)) {
					if (checkCaseStatusIsConfirming(currentStatus, nextStatus)) {
						model.addAttribute("nextStatus", nextStatus);
					} else {
						throw new ResourceNotFoundException();
					}
				} else {
					if (checkCaseStatusIsNg(currentStatus, nextStatus)) {
						model.addAttribute("nextStatus", nextStatus);
					} else {
						throw new ResourceNotFoundException();
					}

				}
			}

			model.addAttribute("request", request);

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

				String currentStatus = request.getStatus();
				String nextStatus = httpRequest.getParameter("selected_next_status");

				if (currentStatus.equals(Constants.STATUS_CONFIRMING) && !checkCaseStatusIsConfirming(currentStatus, nextStatus)) {
					messageId = "WRN2";
				} else if (currentStatus.equals(Constants.STATUS_NG) && !checkCaseStatusIsNg(currentStatus, nextStatus)) {
					messageId = "WRN2";
				} else {
					if (currentStatus.equals(Constants.STATUS_CONFIRMING) || currentStatus.equals(Constants.STATUS_NG)) {
						request.setStatus(nextStatus);
					} else if (currentStatus.equals(Constants.STATUS_NEW)) {
						request.setStatus(Constants.STATUS_CONFIRMING);
					} else if (currentStatus.equals(Constants.STATUS_OK)) {
						request.setStatus(Constants.STATUS_PROCESSING);
					} else if (currentStatus.equals(Constants.STATUS_PROCESSING)) {
						request.setStatus(Constants.STATUS_FINISHED);
					} else {
						// show message
					}

					// create session
					session = sqlSessionFactory.openSession();

					requestDAL.setSession(session);

					// get old information before update
					RelationRequest oldRequest = requestDAL.get(request.getRelation_request_id());
					RequestChangeInfo oldInfo = setInfo(oldRequest);

					// update request
					requestDAL.updateRequest(request);

					// get new information after update
					RelationRequest newRequest = requestDAL.get(request.getRelation_request_id());
					RequestChangeInfo newInfo = setInfo(newRequest);

					// open sql session
					commentDAL = DALFactory.getDAL(CommentDAL.class, sqlSessionFactory);
					commentDAL.setSession(session);

					// Insert into table comment
					commentDAL.updateRequest(oldInfo, newInfo, newRequest);

					session.commit();

					// Display information message when delete successful
					messageId = "INF1";
					success = true;
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
	
	private RequestChangeInfo setInfo(RelationRequest request) {
		RequestChangeInfo requestChangeInfo = new RequestChangeInfo();
		requestChangeInfo.setRelation_request_id(request.getRelation_request_id());
		requestChangeInfo.setStatus(request.getStatus());
		requestChangeInfo.setStatus_description(request.getStatus_description());
		requestChangeInfo.setDirector_id(request.getAssign_user_id());
		requestChangeInfo.setDirector_name(request.getAssign_user_name());
		requestChangeInfo.setRenkei_date(request.getCrawl_date());
		
		return requestChangeInfo;
	}

	private boolean checkCaseStatusIsConfirming(String currentStatus, String nextStatus) {
		if (Validator.isNullOrEmpty(nextStatus)) {
			return false;
		} else {
			if (nextStatus.equals(Constants.STATUS_OK) || nextStatus.equals(Constants.STATUS_NG)) {
				return true;
			} else {
				return false;
			}
		}
	}

	private boolean checkCaseStatusIsNg(String currentStatus, String nextStatus) {
		if (Validator.isNullOrEmpty(nextStatus)) {
			return false;
		} else {
			if (nextStatus.equals(Constants.STATUS_CONFIRMING) || nextStatus.equals(Constants.STATUS_DELETED)) {
				return true;
			} else {
				return false;
			}
		}
	}

	@RequestMapping(value = "/confirm_update_director/", method = RequestMethod.POST)
	public ModelAndView confirmUpdateDirector(HttpServletRequest httpRequest, ModelMap model) throws RuntimeException {
		try {
			ViewBuilder builder = getViewBuilder("request.confirm-update-director", model);
			int requestId = Integer.parseInt(httpRequest.getParameter("relation_request_id"));
			String directorId = httpRequest.getParameter("new_director_id");

			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			RelationRequestDAL requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);

			if (request == null || directorId == null) {
				throw new ResourceNotFoundException();
			}

			int newDirectorId = Integer.parseInt(directorId);

			// get User detail
			UserDAL userDAL = DALFactory.getDAL(UserDAL.class, sqlSessionFactory);
			User newDirector = userDAL.get(newDirectorId);

			if (newDirector == null) {
				throw new ResourceNotFoundException();
			}

			model.addAttribute("request", request);
			model.addAttribute("newDirectorId", newDirectorId);

			return view(builder);
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException(e, this.getClass());
		} catch (GenericException e) {
			throw new BadRequestException(e, this.getClass());
		}
	}

	@ResponseBody
	@RequestMapping(value = "/update_director/", method = RequestMethod.POST)
	public String submitUpdateDirector(HttpServletRequest httpRequest, ModelMap model, RedirectAttributes redirectAttributes) throws ResourceNotFoundException {

		String messageId = null;
		int currentRequestId = 0;
		boolean success = false;
		SqlSession session = null;
		RelationRequestDAL requestDAL = null;
		CommentDAL commentDAL = null;

		try {
			int requestId = Integer.parseInt(httpRequest.getParameter("relation_request_id"));
			int newDirectorId = Integer.parseInt(httpRequest.getParameter("new_director_id"));

			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);

			UserDAL userDAL = DALFactory.getDAL(UserDAL.class, sqlSessionFactory);

			User user = userDAL.get(newDirectorId);

			if (request == null || user == null) {
				// inform error message about invalid data
				messageId = "WRN2";
			} else {

				request.setAssign_user_id(newDirectorId);

				// create session
				session = sqlSessionFactory.openSession();

				requestDAL.setSession(session);

				// get old information before update
				RelationRequest oldRequest = requestDAL.get(request.getRelation_request_id());
				RequestChangeInfo oldInfo = setInfo(oldRequest);
				
				// update request
				requestDAL.updateOnlyDirectorOfRequest(request);

				// get new information after update
				RelationRequest newRequest = requestDAL.get(request.getRelation_request_id());
				RequestChangeInfo newInfo = setInfo(newRequest);

				// open sql session
				commentDAL = DALFactory.getDAL(CommentDAL.class, sqlSessionFactory);
				commentDAL.setSession(session);

				// Insert into table comment
				commentDAL.updateRequest(oldInfo, newInfo, newRequest);

				session.commit();

				// Display information message when delete successful
				messageId = "INF1";
				success = true;
				currentRequestId = newRequest.getRelation_request_id();
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
			map.put("url", "/request/view_request/" + currentRequestId + "/");
		}

		return GSON.toJson(map);
	}
}
