package com.hrs.mediarequesttool.common;

public class Constants {
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static int MAX_LENGTH_COMMENT = 255;
	
	public final static String STATUS_NEW = "NEW";
	public final static String STATUS_CONFIRMING = "CONFIRMING";
	public final static String STATUS_OK = "OK";
	public final static String STATUS_NG = "NG";
	public final static String STATUS_DELETED = "DELETED";
	public final static String STATUS_PROCESSING = "PROCESSING";
	public final static String STATUS_FINISHED = "FINISHED";
	public final static String STATUS_DESTROYED = "DESTROYED";
	
	public final static String[] NEXT_CONFIRMING = {"OK", "NG"};
	public final static String[] NEXT_NG = {"CONFIRMING", "DELETED"};
	
	public final static int MAX_LENGTH_ASSIGNED_PERSON = 100;
	public final static int USER_ROLE_OF_DIRECTOR = 1;
	
	public final static String WEBAN_MEDIA_ID = "weban";
	public final static String WEBAN_LOGIN_ID_1 = "Webbox ID";
	public final static String WEBAN_LOGIN_PASSWORD_1 = "パスワード";
	public final static String ANGWS_LOGIN_ID_1 = "利用者ID";
	public final static String ANGWS_LOGIN_ID_2 = "ユーザーID";
	public final static String ANGWS_LOGIN_PASSWORD_1 = "パスワード";
	public final static String ANGWS_LOGIN_PASSWORD_2 = "応募管理者用パスワード";
	
	public final static String FLOW_ID = "flow_id";
	public final static String UKERUKUN_MEDIA_ID = "ukerukun"; // for compare to submit data to Kintone
	
	public final static int USER_ROLE_MEDIA_CHECKER = 0;
	public final static int USER_ROLE_DIRECTOR = 1;
	
	public final static String MSG_INVALID_REQUEST_ID = "Invalid Request (NOT FOUND). RequestID: ";
    public final static String MSG_INVALID_REQUEST_STATUS = "Invalid current status of request (DATA CORRUPT): ";
    public final static String MSG_INVALID_STATUS_ON_SCREEN = "Invalid status on screen (DATA CORRUPT). Status on screen: ";
    public final static String MSG_INVALID_DIRECTOR_ID = "Invalid directorId (DATA CORRUPT). Selected directorId: ";
    public final static String MSG_INVALID_CRAWLDATE = "Invalid crawlDate (DATA CORRUPT). Selected crawlDate: ";
    
    public final static String MSG_INFO_DESTROYED = "他のユーザによりレコードが取消されました。レコードID： ";
}
