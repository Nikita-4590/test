package com.hrs.mediarequesttool.mail;

import org.springframework.ui.ModelMap;
import com.hrs.mediarequesttool.pojos.Comment;

public class HistorySender extends MailSender {
	public static void execute(Comment comment) {
		new HistorySender(comment).start();
	}

	private Comment comment;

	private HistorySender(Comment comment) {
		this.comment = comment;
	}

	@Override
	protected String getTemplateFile() {
		return "history.ftl";
	}

	@Override
	protected ModelMap getModel() {
		ModelMap model = new ModelMap();
		model.put("comment", this.comment);

		return model;
	}

	@Override
	protected String getMailSubject() {
		String subject = "【リクオプ】媒体連携設定依頼を受け付けました";
		return subject;
	}
}
