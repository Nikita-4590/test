package com.hrs.mediarequesttool.mail;

import org.springframework.ui.ModelMap;

import com.hrs.mediarequesttool.pojos.Comment;
import com.hrs.mediarequesttool.pojos.RelationRequest;

public class HistorySender extends MailSender {
	public static void execute(Comment comment, RelationRequest request) {
		new HistorySender(comment, request).start();
	}

	private Comment comment;
	private RelationRequest request;

	private HistorySender(Comment comment, RelationRequest request) {
		this.comment = comment;
		this.request = request;
	}

	@Override
	protected String getTemplateFile() {
		return "history.ftl";
	}

	@Override
	protected ModelMap getModel() {
		ModelMap model = new ModelMap();
		model.put("comment", this.comment);
		model.put("request", this.request);

		return model;
	}

	@Override
	protected String getMailSubject() {
		String subject = "【媒体連携設定依頼】" + this.comment.getUser_name() + "に依頼の情報が変更されました。";
		return subject;
	}
}
