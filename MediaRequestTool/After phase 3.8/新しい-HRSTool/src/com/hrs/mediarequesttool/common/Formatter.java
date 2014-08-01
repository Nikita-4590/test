package com.hrs.mediarequesttool.common;

import javax.servlet.ServletContext;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import com.hrs.mediarequesttool.common.PropertiesLoader;

public class Formatter {
	private final DateTimeFormatter dateFormatter;

	private ServletContext context;

	private Formatter() {

		String inputDateFormat = PropertiesLoader.instance.getInputDateFormat();
		String alterInputDateFormat = PropertiesLoader.instance.getAlterInputDateFormater();

		DateTimeParser[] parsers = { 
			DateTimeFormat.forPattern(inputDateFormat).getParser(), 
			DateTimeFormat.forPattern(alterInputDateFormat).getParser() 
		};

		// 

		dateFormatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();
	}

	private static Formatter formatter;

	public static Formatter getInstance(ServletContext context) {
		if (formatter == null)
			formatter = new Formatter();

		formatter.context = context;
		return formatter;
	}

	/* -- END CONSTRUCTOR -- */

	public String link(String url, String text) {
		return link(url, text, null, null);
	}

	public String link(String url, String text, String id) {
		return link(url, text, id, null);
	}

	public String link(String url, String text, String id, String className) {
		StringBuilder output = new StringBuilder();
		output.append("<a href=\"").append(this.context.getContextPath()).append(url).append("\"");
		if (id != null && !id.isEmpty()) {
			output.append(" id=\"").append(id).append("\"");
		}
		if (className != null && !className.isEmpty()) {
			output.append(" class=\"").append(className).append("\"");
		}

		output.append(">").append(text).append("</a>");
		return output.toString();
	}

	public String createPageNavigation(int currentPage, int totalPage, String url) {
		try {
			StringBuilder output = new StringBuilder();

			if (currentPage > 0) {
				// display first, previous
				output.append(this.link(url.replaceAll("\\{page\\}", "0"), "<<"));
				output.append("&nbsp;");
				output.append(this.link(url.replaceAll("\\{page\\}", String.valueOf(currentPage - 1)), "<"));
				output.append("&nbsp;");
			}
			// display {currentPage}/{totalPage}
			output.append(currentPage + 1).append("/").append(totalPage);

			if (currentPage < totalPage - 1) {
				// display next, last
				output.append("&nbsp;");
				output.append(this.link(url.replaceAll("\\{page\\}", String.valueOf(currentPage + 1)), ">"));
				output.append("&nbsp;");
				output.append(this.link(url.replaceAll("\\{page\\}", String.valueOf(totalPage - 1)), ">>"));
			}
			return output.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String createBreadcrumbs(Breadcrumb[] breadcrumbs) {
		if (breadcrumbs == null || breadcrumbs.length == 0) {
			return "";
		} else {
			StringBuilder output = new StringBuilder();

			for (int i = 0; i < breadcrumbs.length; i++) {
				if (breadcrumbs[i].hasLink()) {
					output.append(link(breadcrumbs[i].getUrl(), breadcrumbs[i].getText()));
				} else {
					output.append("<span>").append(breadcrumbs[i].getText()).append("</span>");
				}
				if (i < breadcrumbs.length - 1) {
					output.append("<span>&nbsp;&gt;&nbsp;</span>");
				}
			}

			return output.toString();

		}
	}

	public String limit(String input, int length) {
		if (length > 3 && input.length() > length) {
			return this.concat(input.substring(0, length - 3), "。。。");
		} else {
			return input;
		}
	}

	public String concat(Object... args) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < args.length; i++) {
			builder.append(args[i]);
		}

		return builder.toString();
	}

	public String formatDate(String input) {
		try {
			String outputDateFormat = PropertiesLoader.instance.getOutputDateFormat();

			DateTime time = dateFormatter.parseDateTime(input);

			return time.toString(outputDateFormat);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return input;
	}

	public String url(String url) {
		return this.context.getContextPath() + url;
	}

	public String url(Object... urlFragments) {
		return this.url(this.concat(urlFragments));
	}
}
