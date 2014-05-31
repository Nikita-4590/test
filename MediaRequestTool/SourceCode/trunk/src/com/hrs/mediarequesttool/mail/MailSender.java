package com.hrs.mediarequesttool.mail;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

import com.hrs.mediarequesttool.common.Formatter;
import com.hrs.mediarequesttool.common.PropertiesLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;

public abstract class MailSender extends Thread {

	private static String TEMPLATE_PATH;
	private static Formatter FORMATTER;
	private static Configuration CONFIG;

	public static void init(ServletContext context) {
		TEMPLATE_PATH = context.getRealPath("/WEB-INF/view/mail/");
		FORMATTER = Formatter.getInstance(context);

		try {
			CONFIG = new Configuration();
			CONFIG.setDefaultEncoding("UTF-8");
			CONFIG.setNumberFormat("#");

			File folder = new File(TEMPLATE_PATH);
			CONFIG.setDirectoryForTemplateLoading(folder);
		} catch (Exception e) {

			CONFIG = null;
		}
	}

	private Logger logger = Logger.getLogger(MailSender.class);

	protected MailSender() {
	}

	@Override
	public void run() {
		sendMail();
	}

	private void sendMail() {
		Properties props = new Properties();
		props.put("mail.smtp.user", PropertiesLoader.instance.getMailAddress());
		props.put("mail.smtp.host", PropertiesLoader.instance.getMailHost());
		props.put("mail.smtp.port", PropertiesLoader.instance.getMailPort());
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.socketFactory.port", PropertiesLoader.instance.getMailPort());
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		try {
			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance(props, auth);
			session.setDebug(true);

			ModelMap model = getModel();
			model.put("formatter", FORMATTER);

			MimeMessage msg = new MimeMessage(session);
			msg.setSubject(getMailSubject(), "utf-8");
			msg.setText(fromTemplate(model), "utf-8", "html");
			msg.setFrom(new InternetAddress(PropertiesLoader.instance.getMailAddress()));
			msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(PropertiesLoader.instance.getToAddress()));
			msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(PropertiesLoader.instance.getCcAddress()));
			msg.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(PropertiesLoader.instance.getBccAddress()));

			Transport.send(msg);

		} catch (Exception mex) {
			logger.error(mex);
		}
	}

	private String fromTemplate(ModelMap model) throws IOException {
		StringWriter writer = new StringWriter();
		try {
			Template template = CONFIG.getTemplate(getTemplateFile());
			template.process(model, writer);
			String content = writer.toString();
			writer.close();
			return content;
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			if (writer != null) {
				writer.close();
				writer = null;
			}
		}
	}

	protected abstract String getTemplateFile();

	protected abstract ModelMap getModel();

	protected abstract String getMailSubject();

	private class SMTPAuthenticator extends Authenticator {
		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(PropertiesLoader.instance.getMailAddress(), PropertiesLoader.instance.getMailPassword());
		}
	}
}
