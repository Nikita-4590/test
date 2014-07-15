package com.hrs.mediarequesttool.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.hrs.mediarequesttool.kintone.API;
import com.hrs.mediarequesttool.kintone.exception.KintoneException;
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
	public ModelAndView list(HttpServletRequest httpRequest, ModelMap model, Authentication authentication, RedirectAttributes redirectAttributes, HttpSession session) throws GenericException {

		/*
		 * add function check request id
		 * httpRequest.getAttribute(Constants.STORED_REQUEST_ID) == id of
		 * httprequest
		 */
		createFlowId(httpRequest, model, authentication, session);

		ViewBuilder viewBuilder = null;
		try {
			
			StatusDAL statusDAL = getDAL(StatusDAL.class);
			Role role = new Role(authentication.getPrincipal());
			List<Status> listStatus = statusDAL.getAll(role.getUnReadStatus());
			model.addAttribute("listStatus", listStatus);

			viewBuilder = getViewBuilder("request.list", model);
			viewBuilder.setScripts("request.list.js");
			viewBuilder.setStylesheets("request.list.css", "global.css");
			viewBuilder.setPageTitle("依頼一覧");
		
		} catch (GenericException e) {
			e.printStackTrace();
			return fallbackToRequestList(httpRequest, redirectAttributes, e);
		}
		return view(viewBuilder);
	}

	@RequestMapping(value = "/ajax_list/")
	@ResponseBody
	public ModelAndView ajaxList(HttpServletRequest httpRequest, ModelMap model, Authentication authentication, HttpSession session) {
		try {

			String pageParam = httpRequest.getParameter("page");
			String sortParam = httpRequest.getParameter("sort");
			String searchParam = httpRequest.getParameter("searchText");
			String statusParam = httpRequest.getParameter("status");
			String directionParam = httpRequest.getParameter("direction");
			String firstLoad = httpRequest.getParameter("firstload");
			int page = pageParam == null ? -1 : Integer.parseInt(pageParam);
			
			
			RelationRequestDAL requestDAL = getDAL(RelationRequestDAL.class);
			
			Role role = new Role(authentication.getPrincipal());
			
			PagingResult<RelationRequest> relationRequests = null;
			
			String flowId = httpRequest.getParameter(Constants.FLOW_ID);
			
			if (firstLoad != null) {
				Object search = session.getAttribute(flowId);
				SearchObject searchObject = (SearchObject) search;
				
				if (searchObject != null && searchObject.getDirection() != null && searchObject.getSort() != null) {
					model.addAttribute("sort", searchObject.getSort());
					model.addAttribute("direction", searchObject.getDirection());
					
					if (searchObject.getStatus() != null && searchObject.getSearchText() != null) {
						
						relationRequests = requestDAL.getAllRecord(searchObject.getPage(), searchObject.getDirection(), searchObject.getSort(), searchObject.getSearchText(), searchObject.getStatus(),
								role.getPriority(), role.getUnReadStatus());

						model.addAttribute("searchText", searchObject.getSearchText());
						model.addAttribute("searchStatus", searchObject.getStatus());
					
					} else {
						
						relationRequests = requestDAL.paging(searchObject.getPage(), searchObject.getDirection(), searchObject.getSort(), role.getRoles(), role.getPriority());
					}
				} else {
					relationRequests = requestDAL.paging(page, directionParam, sortParam, role.getRoles(), role.getPriority());
				}
			} else {
				if (flowId == null || flowId.contains("undefined")) {
					
					flowId = this.generateFlowId(authentication.getPrincipal());
					
					model.addAttribute("flowId", flowId);
				} else {
					model.addAttribute("flowId", flowId);
				}
				
				SearchObject searchObject = new SearchObject();
				
				if (statusParam != null || searchParam != null) {
					
					searchObject.setPage(page);
					searchObject.setDirection(directionParam);
					searchObject.setSort(sortParam);
					searchObject.setSearchText(searchParam);
					searchObject.setStatus(statusParam);
					
					relationRequests = requestDAL.getAllRecord(page, directionParam, sortParam, searchParam, statusParam, role.getPriority(), role.getUnReadStatus());
				} else {
					
					searchObject.setPage(page);
					searchObject.setDirection(directionParam);
					searchObject.setSort(sortParam);
					
					relationRequests = requestDAL.paging(page, directionParam, sortParam, role.getRoles(), role.getPriority());
				}
				session.setAttribute(flowId, searchObject);
			}
			
			model.addAttribute("relationRequests", relationRequests);
			model.addAttribute("compare_status", role.getHightLight());
			model.addAttribute("currentUser", role.getUserID());
			model.addAttribute("isDirector", role.isDirector());
		
		} catch (GenericException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view("request.ajax_list", model);
	}

	@RequestMapping(value = "/view_request/{relation_request_id}/")
	@ResponseBody
	public ModelAndView viewRequest(HttpServletRequest httpRequest, @PathVariable("relation_request_id") int requestId, ModelMap model, RedirectAttributes redirectAttributes) {

		// for link with Request List page
		setFlowId(httpRequest, model);
		
		ViewBuilder builder = getViewBuilder("request.detail", model);

		try {
			
			// get data from database
			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			RelationRequestDAL requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);

			if (request == null) {
				return fallbackToRequestList(httpRequest, redirectAttributes, new IllegalArgumentException(Constants.LOG_INVALID_REQUEST_ID + requestId));
			}

			StatusDAL statusDAL = DALFactory.getDAL(StatusDAL.class, sqlSessionFactory);
			Status nextStatus = new Status();
			String[] listStatus = null;
			String currentStatus = request.getStatus();

			List<Status> listNextStatus = new ArrayList<Status>();
			
			if (currentStatus.equals(Constants.STATUS_DELETED)) {
				return fallbackToRequestList(httpRequest, redirectAttributes, new IllegalArgumentException(Constants.MSG_INFO_DELETED + requestId));
			} else if (currentStatus.equals(Constants.STATUS_DESTROYED)) {
				return fallbackToRequestList(httpRequest, redirectAttributes, new IllegalArgumentException(Constants.MSG_INFO_DESTROYED + requestId));
			} else if (currentStatus.equals(Constants.STATUS_NEW)) {
				
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

				// get list directors except current assign_user_id
				List<User> directors = userDAL.getListDirector(request.getAssign_user_id());
				model.addAttribute("directors", directors);

			} else if (currentStatus.equals(Constants.STATUS_FINISHED)) {
				model.addAttribute("view", Constants.STATUS_FINISHED);
			} else {
				return fallbackToRequestList(httpRequest, redirectAttributes, new IllegalArgumentException(Constants.LOG_INVALID_REQUEST_STATUS + request.getStatus()));
			}

			if (nextStatus != null) {
				model.addAttribute("nextStatus", nextStatus);
			}

			MediaLabelDAL mediaLabelDAL = DALFactory.getDAL(MediaLabelDAL.class, sqlSessionFactory);

			MediaLabel mediaLabel = mediaLabelDAL.get(request.getMedia_id());

			if (mediaLabel.getMedia_id().equals(Constants.WEBAN_MEDIA_ID)) {
				if (!Validator.isNullOrEmpty(request.getLogin_id_2()) && !Validator.isNullOrEmpty(request.getLogin_password_2())) {
					mediaLabel.setLogin_id_1(Constants.ANGWS_LOGIN_ID_1);
					mediaLabel.setLogin_id_2(Constants.ANGWS_LOGIN_ID_2);
					mediaLabel.setLogin_password_1(Constants.ANGWS_LOGIN_PASSWORD_1);
					mediaLabel.setLogin_password_2(Constants.ANGWS_LOGIN_PASSWORD_2);
				} else {
					mediaLabel.setLogin_id_1(Constants.WEBAN_LOGIN_ID_1);
					mediaLabel.setLogin_id_2(null);
					mediaLabel.setLogin_password_1(Constants.WEBAN_LOGIN_PASSWORD_1);
					mediaLabel.setLogin_password_2(null);
				}
			}

			model.addAttribute("request", request);
			model.addAttribute("mediaLabel", mediaLabel);
			model.addAttribute("show_button_list", "on");

			builder.setPageTitle("依頼詳細");
			builder.setStylesheets("global.form.css", "request.detail.css");
			builder.setScripts("jquery/jquery.form.min.js", "request.detail.js", "request.change.js", "request.update.director.js", "request.destroy.js");

		} catch (GenericException e) {
			e.printStackTrace();
			return fallbackToRequestList(httpRequest, redirectAttributes, e);
		}

		return view(builder);
	}

	@RequestMapping(value = "/confirm_change/", method = RequestMethod.POST)
	public ModelAndView confirmChange(HttpServletRequest httpRequest, ModelMap model) throws RuntimeException {
		
		//for link with Request List page
		setFlowId(httpRequest, model);
		
		try {
			ViewBuilder builder = getViewBuilder("request.confirm-change", model);
			
			int requestId = Integer.parseInt(httpRequest.getParameter("relation_request_id"));
			String currentStatusOnScreen = httpRequest.getParameter("current_status");

			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			RelationRequestDAL requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);

			if (request == null) {
				throw new BadRequestException(Constants.LOG_INVALID_REQUEST_ID + requestId);
			}

			String currentStatus = request.getStatus();

			if (!currentStatus.equals(currentStatusOnScreen)) {
				// Status may be changed. Show message dialog and reload the detail page
				throw new ResourceNotFoundException();
			} else if (currentStatus.equals(Constants.STATUS_CONFIRMING) || currentStatus.equals(Constants.STATUS_NG)) {
				String nextStatus = httpRequest.getParameter("selected_next_status");

				if (currentStatus.equals(Constants.STATUS_CONFIRMING)) {
					if (checkCaseStatusIsConfirming(nextStatus)) {
						model.addAttribute("nextStatus", nextStatus);
					} else {
						throw new BadRequestException(Constants.LOG_INVALID_NEXT_STATUS_OF_CONFIRMING + nextStatus);
					}
				} else {
					if (checkCaseStatusIsNg(nextStatus)) {
						model.addAttribute("nextStatus", nextStatus);
					} else {
						throw new BadRequestException(Constants.LOG_INVALID_NEXT_STATUS_OF_NG + nextStatus);
					}
				}
			} else if (currentStatus.equals(Constants.STATUS_OK)) {
				String directorId = httpRequest.getParameter("new_director_id");
				if (!validateNewDirectorId(directorId)) {
					throw new BadRequestException(Constants.LOG_INVALID_DIRECTOR_ID + directorId);
				} else {

					model.addAttribute("newDirectorId", directorId);
					model.addAttribute("nextStatus", Constants.STATUS_PROCESSING);
				}
			} else if (currentStatus.equals(Constants.STATUS_PROCESSING)) {
				String crawlDate = httpRequest.getParameter("crawl_date");
				if (!validateCrawlDate(crawlDate)) {
					throw new BadRequestException(Constants.LOG_INVALID_CRAWLDATE + crawlDate);
				} else {
					model.addAttribute("crawlDate", crawlDate);
					model.addAttribute("nextStatus", Constants.STATUS_FINISHED);
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

		//for link with Request List page
		setFlowId(httpRequest, model);

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
				// inform error message about invalid data. Not found
				messageId = "ERR153";
				throw new BadRequestException(Constants.LOG_INVALID_REQUEST_ID + requestId);
			} else {
				String currentStatus = request.getStatus();
				String currentStatusOnScreen = httpRequest.getParameter("current_status");
				
				if (!currentStatus.equals(currentStatusOnScreen)) {
					// Status may be changed. Show message dialog and reload the detail page
					messageId = "ERR151";
				} else {
					String nextStatus = httpRequest.getParameter("selected_next_status");
					String directorId = httpRequest.getParameter("new_director_id");
					String crawlDate = httpRequest.getParameter("crawl_date");
					String comment = httpRequest.getParameter("backToConfirming-comment");

					if (currentStatus.equals(Constants.STATUS_CONFIRMING) && !checkCaseStatusIsConfirming(nextStatus)) {
						messageId = "ERR153";
						throw new BadRequestException(Constants.LOG_INVALID_NEXT_STATUS_OF_CONFIRMING + nextStatus);
						
					} else if (currentStatus.equals(Constants.STATUS_NG) && !checkCaseStatusIsNg(nextStatus)) {
						messageId = "ERR153";
						throw new BadRequestException(Constants.LOG_INVALID_NEXT_STATUS_OF_NG + nextStatus);
						
					} else if (currentStatus.equals(Constants.STATUS_NG) && nextStatus.equals(Constants.STATUS_CONFIRMING) && !validateComment(comment)) {
						messageId = "ERR153";
						throw new BadRequestException(Constants.LOG_INVALID_COMMENT + comment);
						
					} else if (currentStatus.equals(Constants.STATUS_OK) && !validateNewDirectorId(directorId)) {
						messageId = "ERR153";
						throw new BadRequestException(Constants.LOG_INVALID_DIRECTOR_ID + directorId);
						
					} else if (currentStatus.equals(Constants.STATUS_PROCESSING) && !validateCrawlDate(crawlDate)) {
						messageId = "ERR153";
						throw new BadRequestException(Constants.LOG_INVALID_CRAWLDATE + crawlDate);
						
					} else {
						if (currentStatus.equals(Constants.STATUS_CONFIRMING) || currentStatus.equals(Constants.STATUS_NG)) {
							request.setStatus(nextStatus);
						} else if (currentStatus.equals(Constants.STATUS_NEW)) {
							request.setStatus(Constants.STATUS_CONFIRMING);
						} else if (currentStatus.equals(Constants.STATUS_OK)) {
							int newDirectorId = Integer.parseInt(directorId);
							request.setAssign_user_id(newDirectorId);
							request.setStatus(Constants.STATUS_PROCESSING);
						} else if (currentStatus.equals(Constants.STATUS_PROCESSING)) {
							request.setCrawl_date(crawlDate);
							request.setStatus(Constants.STATUS_FINISHED);
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
						RequestChangeInfo newInfo = setInfo(newRequest); // to send Email

						// renkei with API Kintone
						if (currentStatus.equals(Constants.STATUS_PROCESSING) && nextStatus.equals(Constants.STATUS_FINISHED)) {
							if (newRequest.getMedia_id().equals(Constants.UKERUKUN_MEDIA_ID)) {
								new API().post(newRequest, true);
							} else {
								new API().post(newRequest, false);
							}
						}

						// open sql session
						commentDAL = DALFactory.getDAL(CommentDAL.class, sqlSessionFactory);
						commentDAL.setSession(session);

						// Insert into table comment - including send Email
						commentDAL.updateRequest(comment, oldInfo, newInfo, newRequest);

						session.commit();

						// Display information message when change successful
						success = true;
					}
				}
			}

		} catch (NumberFormatException e) {
			// inform invalid data
			messageId = "ERR153";
			e.printStackTrace();
		} catch (GenericException e) {
			// inform error message about access database failure
			messageId = "ERR150";
		} catch (KintoneException e) {
			// inform error message about cannot submit to Kintone
			messageId = "ERR154";
			e.printStackTrace();
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

		// if (success) {
		// map.put("url", "/request/list/");
		// }

		return GSON.toJson(map);
	}

	private RequestChangeInfo setInfo(RelationRequest request) {
		RequestChangeInfo requestChangeInfo = new RequestChangeInfo();
		requestChangeInfo.setStatus_description(request.getStatus_description());
		requestChangeInfo.setDirector_name(request.getAssign_user_name());
		requestChangeInfo.setRenkei_date(request.getCrawl_date_to_display());

		return requestChangeInfo;
	}
	
	private boolean checkCaseStatusIsConfirming(String nextStatus) {
		if (Validator.isNullOrEmpty(nextStatus)) {
			return false;
		} else {
			return nextStatus.equals(Constants.STATUS_OK) || nextStatus.equals(Constants.STATUS_NG);
		}
	}

	private boolean checkCaseStatusIsNg(String nextStatus) {
		if (Validator.isNullOrEmpty(nextStatus)) {
			return false;
		} else {
			return nextStatus.equals(Constants.STATUS_CONFIRMING) || nextStatus.equals(Constants.STATUS_DELETED);
		}
	}

	private boolean validateNewDirectorId(String directorId) {
		if (Validator.isNullOrEmpty(directorId)) {
			return false;
		} else {
			try {
				int newDirectorId = Integer.parseInt(directorId);

				SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

				// get User detail
				UserDAL userDAL = DALFactory.getDAL(UserDAL.class, sqlSessionFactory);
				User newDirector = userDAL.get(newDirectorId);

				if (newDirector == null) {
					return false;
				} else {
					return true;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return false;
			} catch (GenericException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	private boolean validateCrawlDate(String stringCrawlDate) {
		if (Validator.isNullOrEmpty(stringCrawlDate)) {
			return false;
		} else {
			try {
				DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(Constants.DATE_FORMAT);
				DateTime crawlDate = DateTime.parse(stringCrawlDate, dateFormatter);
				DateTime tomorrow = DateTime.now().plusDays(1).withTime(0, 0, 0, 0);

				return (crawlDate.isAfter(tomorrow) || crawlDate.isEqual(tomorrow));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	@RequestMapping(value = "/confirm_update_director/", method = RequestMethod.POST)
	public ModelAndView confirmUpdateDirector(HttpServletRequest httpRequest, ModelMap model) throws RuntimeException {
		
		//for link with Request List page
		setFlowId(httpRequest, model);

		try {
			ViewBuilder builder = getViewBuilder("request.confirm-update-director", model);
			
			int requestId = Integer.parseInt(httpRequest.getParameter("relation_request_id"));
			int currentDirectorIdOnView = Integer.parseInt(httpRequest.getParameter("current_director_id"));
			String directorId = httpRequest.getParameter("new_director_id");

			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			RelationRequestDAL requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);
			
			if (request == null) {
				throw new BadRequestException(Constants.LOG_INVALID_REQUEST_ID + requestId);
			} else if (!validateNewDirectorId(directorId)) {
				throw new BadRequestException(Constants.LOG_INVALID_DIRECTOR_ID + directorId);
			} else if (currentDirectorIdOnView != request.getAssign_user_id() || !request.getStatus().equals(Constants.STATUS_PROCESSING)) {
				throw new ResourceNotFoundException();
			}

			int newDirectorId = Integer.parseInt(directorId);

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
		
		//for link with Request List page
		setFlowId(httpRequest, model);
		
		String messageId = null;
		boolean success = false;
		SqlSession session = null;
		RelationRequestDAL requestDAL = null;
		CommentDAL commentDAL = null;

		try {
			int requestId = Integer.parseInt(httpRequest.getParameter("relation_request_id"));
			int currentDirectorIdOnView = Integer.parseInt(httpRequest.getParameter("current_director_id"));
			String directorId = httpRequest.getParameter("new_director_id");

			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);
			
			if (request == null) {
				// inform error message about invalid data
				messageId = "ERR203";
				throw new BadRequestException(Constants.LOG_INVALID_REQUEST_ID + requestId);
				
			} else if (!validateNewDirectorId(directorId)) {
				// inform error message about invalid data
				messageId = "ERR203";
				throw new BadRequestException(Constants.LOG_INVALID_DIRECTOR_ID + directorId);
				
			} else if (currentDirectorIdOnView != request.getAssign_user_id() || !request.getStatus().equals(Constants.STATUS_PROCESSING)) {
				// Status or current directorId may be changed. Show message dialog and reload the detail page
				messageId = "ERR201";
				
			} else {
				int newDirectorId = Integer.parseInt(directorId);
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
				commentDAL.updateRequest(null, oldInfo, newInfo, newRequest);

				session.commit();

				// Display information message when update director successful
				success = true;
			}

		} catch (NumberFormatException e) {
			// inform error message about invalid data
			messageId = "ERR203";
			e.printStackTrace();
		} catch (GenericException e) {
			// inform error message about access database failure
			messageId = "ERR200";
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

		return GSON.toJson(map);
	}

	@RequestMapping(value = "/confirm_destroy/", method = RequestMethod.POST)
	public ModelAndView confirmDestroy(HttpServletRequest httpRequest, ModelMap model) throws RuntimeException {
		
		//for link with Request List page
		setFlowId(httpRequest, model);

		try {
			ViewBuilder builder = getViewBuilder("request.confirm-destroy", model);
			int requestId = Integer.parseInt(httpRequest.getParameter("relation_request_id"));
			String currentStatusOnScreen = httpRequest.getParameter("current_status");

			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			RelationRequestDAL requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);
			
			if (request == null) {
				throw new BadRequestException(Constants.LOG_INVALID_REQUEST_ID + requestId);
			} else {
				if (!request.getStatus().equals(currentStatusOnScreen)) {
					// Status may be changed. Show message dialog and reload the detail page 
					throw new ResourceNotFoundException();
				} else {
					model.addAttribute("request", request);
				}
			}

			return view(builder);
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException(e, this.getClass());
		} catch (GenericException e) {
			throw new BadRequestException(e, this.getClass());
		}
	}

	@ResponseBody
	@RequestMapping(value = "/destroy/", method = RequestMethod.POST)
	public String submitDestroy(HttpServletRequest httpRequest, ModelMap model, RedirectAttributes redirectAttributes) throws ResourceNotFoundException {

		//for link with Request List page
		setFlowId(httpRequest, model);

		String messageId = null;
		boolean success = false;
		SqlSession session = null;
		RelationRequestDAL requestDAL = null;
		CommentDAL commentDAL = null;

		try {
			int requestId = Integer.parseInt(httpRequest.getParameter("relation_request_id"));
			String currentStatusOnScreen = httpRequest.getParameter("current_status");
			String comment = httpRequest.getParameter("destroy-comment");

			SqlSessionFactory sqlSessionFactory = DBConnection.getSqlSessionFactory(this.servletContext, DBConnection.DATABASE_PADB_PUBLIC, false);

			// get Request detail
			requestDAL = DALFactory.getDAL(RelationRequestDAL.class, sqlSessionFactory);

			RelationRequest request = requestDAL.get(requestId);
			
			if (request == null) {
				messageId = "ERR253";
				throw new BadRequestException(Constants.LOG_INVALID_REQUEST_ID + requestId);
			} else {
				if (!request.getStatus().equals(currentStatusOnScreen)) {
					// Status may be changed. Show message dialog and reload the detail page
					messageId = "ERR251";
					throw new ResourceNotFoundException();
					
				} else {
					if (!validateComment(comment)) {
						messageId = "ERR253";
						throw new BadRequestException(Constants.LOG_INVALID_COMMENT + comment);
						
					} else {
						model.addAttribute("request", request);
						// create session
						session = sqlSessionFactory.openSession();

						requestDAL.setSession(session);

						// get old information before update
						RelationRequest oldRequest = requestDAL.get(request.getRelation_request_id());
						RequestChangeInfo oldInfo = setInfo(oldRequest);

						// update request
						requestDAL.updateRequestToDestroy(request);

						// get new information after update 
						RelationRequest newRequest = requestDAL.get(request.getRelation_request_id());
						RequestChangeInfo newInfo = setInfo(newRequest);

						// open sql session
						commentDAL = DALFactory.getDAL(CommentDAL.class, sqlSessionFactory);
						commentDAL.setSession(session);

						// Insert into table comment
						commentDAL.updateRequest(comment, oldInfo, newInfo, newRequest);

						session.commit();

						// Display information message when destroy successful
						success = true;
					}
				}
			}

		} catch (NumberFormatException e) {
			// inform error message about invalid data
			messageId = "ERR253";
			e.printStackTrace();
		} catch (GenericException e) {
			// inform error message about access database failure
			messageId = "ERR250";
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

	private boolean validateComment(String comment) {
		return !(Validator.isNullOrEmpty(comment) || Validator.checkExceedLength(Constants.MAX_LENGTH_COMMENT, comment));
	}

	protected ModelAndView fallbackToRequestList(HttpServletRequest httpRequest, RedirectAttributes redirectAttributes, Throwable exception) {
		ModelMap model = new ModelMap();
		model.put("exception", exception);
		setSplashAttributes(redirectAttributes, model);
		exception.printStackTrace();
		return redirect("err/");
	}

	private void setFlowId(HttpServletRequest httpRequest, ModelMap model) {
		String flowId = httpRequest.getParameter(Constants.FLOW_ID);
		if (flowId != null && !flowId.equals("undefined")) {
			model.addAttribute("flowId", flowId);
		}
	}

	private void createFlowId(HttpServletRequest httpRequest, ModelMap model, Authentication authentication, HttpSession session) {
		String flowId = httpRequest.getParameter(Constants.FLOW_ID);
		if (flowId == null || flowId.equals("undefined")) {
			
			flowId = this.generateFlowId(authentication.getPrincipal());
			model.addAttribute("flowId", flowId);
			
			session.setAttribute(flowId, new SearchObject());
		} else {
			
			model.addAttribute("flowId", flowId);
		}
	}

	private class SearchObject {
		private int page;
		private String direction;
		private String status;
		private String searchText;
		private String sort;

		public int getPage() {
			return page;
		}

		public void setPage(int page) {
			this.page = page;
		}

		public String getDirection() {
			return direction;
		}

		public void setDirection(String direction) {
			this.direction = direction;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getSearchText() {
			return searchText;
		}

		public void setSearchText(String searchText) {
			this.searchText = searchText;
		}

		public String getSort() {
			return sort;
		}

		public void setSort(String sort) {
			this.sort = sort;
		}

	}
}
