package com.hrs.mediarequesttool.controllers;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;
import com.hrs.mediarequesttool.auth.AuthProvider;
import com.hrs.mediarequesttool.common.Breadcrumb;
import com.hrs.mediarequesttool.common.Formatter;
import com.hrs.mediarequesttool.common.exception.GenericException;
import com.hrs.mediarequesttool.dals.DALFactory;
import com.hrs.mediarequesttool.pojos.User;

public abstract class BaseController {
	protected ModelAndView view(String viewName) {
		return view(viewName, new ModelMap());
	}

	protected Breadcrumb step(String text) {
		return new Breadcrumb(text);
	}

	protected Breadcrumb step(String text, String url) {
		return new Breadcrumb(text, url);
	}

	protected ViewBuilder getViewBuilder(String view) {
		return new ViewBuilder(view);
	}

	protected ViewBuilder getViewBuilder(String view, ModelMap model) {
		return new ViewBuilder(view, model);
	}

	protected ModelAndView view(ViewBuilder viewBuilder) {
		return new ModelAndView(viewBuilder.getView(), viewBuilder.getModel());
	}

	protected ModelAndView view(String viewName, ModelMap model, Breadcrumb... breadcrumbs) {
		injectModel(model);

		// create bread-crumb
		model.addAttribute("breadcrumbs", breadcrumbs);

		// send to view
		return new ModelAndView(viewName, model);
	}

	private void injectModel(ModelMap model) {
		// inject user
		model.addAttribute("user", AuthProvider.getUser());

		// inject formatter
		Formatter formatter = Formatter.getInstance(servletContext);

		model.addAttribute("formatter", formatter);

		// inject helpful variables
		// - context information
		/*
		 * String protocol = httpRequest.isSecure() ? "https://" : "http://"; String serverName = httpRequest.getServerName(); int serverPort = httpRequest.getServerPort(); String contextPath =
		 * httpRequest.getContextPath(); String requestURL = httpRequest.getRequestURI().toString();
		 * 
		 * // create root URL - helpful for build URL in views String rootUrl = formatter.concat(protocol, serverName, ":", serverPort, contextPath);
		 * 
		 * // put to model model.addAttribute("base_root_url", rootUrl); model.addAttribute("base_context_path", servletContext.getContextPath()); model.addAttribute("base_url", requestURL);
		 * model.addAttribute("base_server_name", serverName); model.addAttribute("base_server_port", serverPort); model.addAttribute("base_request_is_secure", httpRequest.isSecure());
		 */
		// add account type contants
	}

	protected ModelAndView redirect(String router) {
		RedirectView view = new RedirectView(servletContext.getContextPath() + "/" + router);

		return new ModelAndView(view);
	}

	protected ModelAndView redirect(String router, ModelMap model) {
		RedirectView view = new RedirectView(servletContext.getContextPath() + "/" + router);
		return new ModelAndView(view, model);
	}

	protected ModelAndView redirect(String router, ModelMap model, boolean exposeAttribute) {
		RedirectView view = new RedirectView(servletContext.getContextPath() + "/" + router);
		//view.setExposeModelAttributes(exposeAttribute);
		view.setExposePathVariables(exposeAttribute);

		return new ModelAndView(view, model);
	}

	protected void setSplashMessage(RedirectAttributes redirectAttributes, String splashMessage) {
		redirectAttributes.addFlashAttribute("splash_message", splashMessage);
	}

	protected void setSplashAttributes(RedirectAttributes redirectAttributes, ModelMap attributes) {
		for (Map.Entry<String, ?> entry : attributes.entrySet()) {
			redirectAttributes.addFlashAttribute(entry.getKey(), entry.getValue());
		}
	}

	protected static final Gson GSON = new Gson();
	@Autowired
	protected ServletContext servletContext;

	// @Autowired
	// protected HttpServletRequest httpRequest;

	public class ViewBuilder {

		public ViewBuilder(String view) {
			this.view = view;
		}

		public ViewBuilder(String view, ModelMap model) {
			this.view = view;
			this.model = model;
		}

		public String getView() {
			return view;
		}

		public void setView(String view) {
			this.view = view;
		}

		public ModelMap getModel() {
			if (model == null) {
				model = new ModelMap();
			}

			// add
			injectModel(model);

			model.addAttribute("scripts", scripts);
			model.addAttribute("stylesheets", stylesheets);
			model.addAttribute("breadcrumbs", breadcrumbs);
			model.addAttribute("pageTitle", pageTitle);

			return model;
		}

		public void setModel(ModelMap model) {
			this.model = model;
		}

		public void setBreadcrumbs(Breadcrumb... breadcrumbs) {
			this.breadcrumbs = breadcrumbs;
		}

		public void setScripts(String... scripts) {
			this.scripts = scripts;
		}

		public void setStylesheets(String... stylesheets) {
			this.stylesheets = stylesheets;
		}

		public void setPageTitle(String pageTitle) {
			this.pageTitle = pageTitle;
		}

		private String view;
		private ModelMap model;
		private Breadcrumb[] breadcrumbs;
		private String[] scripts;
		private String[] stylesheets;
		private String pageTitle;
	}
	
	public <T> T getDAL(Class<T> clazz) throws GenericException{
		return DALFactory.getDAL(clazz, servletContext);
	}
	public User getCurrentUser(){
		return AuthProvider.getUser();
	}
}
