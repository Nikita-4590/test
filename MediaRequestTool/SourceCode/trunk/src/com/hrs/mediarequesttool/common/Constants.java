package com.hrs.mediarequesttool.common;

public class Constants {
	public final static String MAIL_REGEX = "^([a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~\\.])+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|([a-zA-Z0-9]+((\\-[a-zA-Z0-9]+)*)\\.)+[a-zA-Z]{2,})$";
	public final static String MILLISECONDS_REGEX = "^.+?\\.\\d{3}$";
	public final static String MATCHING_TYPE_BYNAME = "byname";
	public final static String MATCHING_TYPE_BYNAME_DESCRIPTION = "社名マッチ";
	public final static String MATCHING_TYPE_BYADDRESS = "byaddress";
	public final static String MATCHING_TYPE_BYADDRESS_DESCRIPTION = "メアドマッチ";
	public final static String FLAG_IS_ENABLED = "連携中";
	public final static String FLAG_IS_DISABLED = "停止中";
	public final static String MAIL_ADDRESS_TYPE_DUMMY = "dummy";
	public final static String MAIL_ADDRESS_TYPE_CLIENT = "client";
	public final static String CHECKBOX_ON = "on";
	public final static String DISABLE_FLAG_IS_TRUE = "1";
	public final static String ENABLE_FLAG_IS_TRUE = "1";
	public final static String MREC = ".mrec.jp";
	public final static String SERVICE_R_RECLOG = "r-reclog";
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static int MAX_LENGTH_EMAIL_ADDRESS = 255;
	public final static int MAX_LENGTH_PASSWORD = 20;
	public final static int MAX_LENGTH_COMMENT = 255;
	public final static int MAX_LENGTH_COMPANY_NAME = 50;
	public final static int MAX_LENGTH_ID = 50;
	public final static int MAX_LENGTH_URL = 500;
	public final static int MAX_LENGTH_FAC_CLIENT_ID = 15;
	public final static int MAX_LENGTH_FAC_ADMIN_ID = 20;
	public final static int MAX_LENGTH_FAC_PASSWORD = 50;

	public final static String MSG_INVALID_UKERUKUN_ID = "Invalid Ukerukun (NOT FOUND)";
	public final static String MSG_INVALID_UKERUKUN_DATA = "Invalid Ukerukun (DATA CORRUPT)";
	public final static String MSG_INVALID_FAC_ID = "Invalid Fac (NOT FOUND)";
	public final static String MSG_INVALID_FAC_DATA = "Invalid Fac (DATA CORRUPT)";
	public final static String MSG_INVALID_COMPANY_ID = "Invalid Company (NOT FOUND)";
	public final static String MSG_INVALID_MEDIA_ID = "Invalid Media (NOT FOUND)";

	public final static String MSG_INVALID_TABAITAI_ID = "Invalid Tabaitai (NOT FOUND)";
	public final static String MSG_INVALID_TABAITAI_DATA = "Invalid Tabaitai (DATA CORRUPT)";

	public final static String DEFAULT_LABEL_ID_1 = "ID １";
	public final static String DEFAULT_LABEL_ID_2 = "ID ２";
	public final static String DEFAULT_LABEL_PASSWORD_1 = "パスワード　１";
	public final static String DEFAULT_LABEL_PASSWORD_2 = "パスワード　２";
	
	public final static int WARNING = 0;
	public final static int ERROR = 1;
	public final static int FATAL = 2;
	public final static int OLD_TABLE_TYPE = 1;
	public final static int DATA_DIFFERENT_TYPE = 2;
	
	public final static String[] NEXT_NEW = {"NEW", "ASSIGNED"};
	public final static String[] NEXT_ASSIGNED = {"ASSIGNED", "CONFIRMING"};
	public final static String[] NEXT_CONFIRMING = {"CONFIRMING", "ASSIGNED", "OK", "NG"};
	public final static String[] NEXT_OK = {"OK", "ASSIGNED", "FINISHED", "DELETED"};
	public final static String[] NEXT_NG = {"NG", "ASSIGNED", "CONFIRMING", "DELETED"};
	public final static String[] NEXT_FINISHED = {"FINISHED"};
	public final static String[] NEXT_DELETED = {"DELETED"};
	public final static int MAX_LENGTH_ASSIGNED_PERSON = 100;
}
