package com.hrs.mediarequesttool.kintone;

import com.hrs.mediarequesttool.common.PropertiesLoader;
import com.hrs.mediarequesttool.common.validator.Validator;
import com.hrs.mediarequesttool.kintone.data.Record;
import com.hrs.mediarequesttool.kintone.data.RecordElement;
import com.hrs.mediarequesttool.kintone.exception.KintoneException;
import com.hrs.mediarequesttool.pojos.RelationRequest;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

class Adapter {
	static public final String GMT_00 = "Atlantic/Azores";
	static public final String DATE_PATTERN = "yyyy-MM-dd";
	static public final String A = "BLOWFISH";

	public Record parse(RelationRequest request, boolean isUkerukun) throws KintoneException {
		Record record = new Record();

		// set common fields
		record.setCompanyName(RecordElement.create(request.getCompany_name()));
		record.setCompanyID(RecordElement.create(correctCompanyID(request.getCompany_id())));
		record.setCrawlDate(RecordElement.create(correctDate(request.getCrawl_date())));
		record.setCrawlDateExtra(RecordElement.create(correctDate(request.getCrawl_date())));
		record.setAssignUser(RecordElement.create(request.getAssign_user_name()));
		record.setCommentForOtherMedia(RecordElement.create(request.getOther_comment()));

		if (isUkerukun) {
			// set ukerukun fields
			record.setMediaType(RecordElement.create(PropertiesLoader.instance.getUkerukunType()));
			record.setLoginIdForUkerukun(RecordElement.create(request.getLogin_id_1()));
			record.setPasswordForUkerukun(RecordElement.create(encode(PropertiesLoader.instance.getPasswordKey(), request.getLogin_password_1())));
		} else {
			// set other-media fields
			record.setMediaType(RecordElement.create(PropertiesLoader.instance.getTabaitaiType()));
			record.setMediaNameForOtherMedia(RecordElement.create(request.getMedia_name()));
			record.setUrlForOtherMedia(RecordElement.create(request.getUrl()));
			record.setLoginID1ForOtherMedia(RecordElement.create(request.getLogin_id_1()));
			record.setLoginID2ForOtherMedia(RecordElement.create(request.getLogin_id_2()));
			record.setPassword1ForOtherMedia(RecordElement.create(encode(PropertiesLoader.instance.getPasswordKey(), request.getLogin_password_1())));
			if (Validator.isNullOrEmpty(request.getLogin_password_2())) {
				record.setPassword2ForOtherMedia(null);
			} else {
				record.setPassword2ForOtherMedia(RecordElement.create(encode(PropertiesLoader.instance.getPasswordKey(), request.getLogin_password_2())));
			}

		}

		return record;
	}

	private String correctDate(String date) throws KintoneException {
		// convert date to yyyy-MM-dd'T'HH:mm:ss'Z' and GMT+0
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_PATTERN);
		DateTime crawlDateTime = DateTime.parse(date, dateTimeFormatter);
		DateTime dtGMT00 = crawlDateTime.withZone(DateTimeZone.forID(GMT_00));
		return dtGMT00.toString();
	}

	private String encode(String key, String text) throws KintoneException {
		try {
			MessageDigest mg = MessageDigest.getInstance("MD5");
			mg.update(key.getBytes());

			SecretKeySpec sksSpec = new SecretKeySpec(mg.digest(), A);
			Cipher cipher = Cipher.getInstance(A + "/ECB/PKCS5Padding");
			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, sksSpec);

			byte[] digest = cipher.doFinal(text.getBytes());
			StringBuffer temp = new StringBuffer();
			for (byte byteChara : digest) {
				temp.append(String.format("%02x", byteChara));
			}
			return temp.toString();
		} catch (Exception e) {
			throw new KintoneException();
		}
	}

	private String correctCompanyID(String companyID) throws KintoneException {
		if (companyID != null) {
			int length = companyID.length();

			if (length <= 8) {
				int extraLength = 8 - length;

				StringBuilder builder = new StringBuilder();

				for (int i = 0; i < extraLength; i++) {
					builder.append("0");
				}

				builder.append(companyID);

				return builder.toString();
			}
		}

		throw new KintoneException();
	}
}
