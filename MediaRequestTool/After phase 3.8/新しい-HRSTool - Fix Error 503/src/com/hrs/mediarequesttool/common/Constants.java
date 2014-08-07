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
	public final static String WEBAN_LOGIN_PASSWORD_1 = "パスワード";
	public final static String ANGWS_LOGIN_ID_1 = "利用者ID";
	public final static String ANGWS_LOGIN_ID_2 = "ユーザーID";
	public final static String ANGWS_LOGIN_PASSWORD_1 = "パスワード";
	public final static String ANGWS_LOGIN_PASSWORD_2 = "応募管理者用パスワード";
	
	public final static String FLOW_ID = "flow_id";
	public final static String UKERUKUN_MEDIA_ID = "ukerukun"; // for compare to submit data to Kintone
	
	public final static int USER_ROLE_MEDIA_CHECKER = 0;
	public final static int USER_ROLE_DIRECTOR = 1;
	
	public final static String LOG_INVALID_REQUEST_ID = "無効なレコード (NOT FOUND)。 レコードID: ";
    public final static String LOG_INVALID_REQUEST_STATUS = "無効なレコードの現在ステータス (DATA CORRUPT)。 ステータス: ";
    public final static String LOG_INVALID_DIRECTOR_ID = "無効な担当ディレクターID (DATA CORRUPT)。 選択されたディレクターID: ";
    public final static String LOG_INVALID_CRAWLDATE = "無効な連携開始日 (DATA CORRUPT)。 選択された連携開始日: ";
    public final static String LOG_INVALID_NEXT_STATUS_OF_CONFIRMING = "無効な選択されたステータス (DATA CORRUPT)。現在のステータス：「接続確認中」。選択されたステータス: )";
    public final static String LOG_INVALID_NEXT_STATUS_OF_NG = "無効な選択されたステータス (DATA CORRUPT)。現在のステータス：「顧客確認中」。選択されたステータス: )";
    public final static String LOG_INVALID_COMMENT = "無効なコメント (DATA CORRUPT). 入力されたコメント: ";
    
    public final static String MSG_INFO_DESTROYED = "他のユーザによりレコードが取消されました。レコードID： ";
    public final static String MSG_INFO_DELETED = "他のユーザによりレコードが削除されました。レコードID： ";
    
    public final static String LIST_PAGE_INFO_DESTROYED = "取消が正常に完了しました。";
    public final static String LIST_PAGE_INFO_DELETED = "削除が正常に完了しました。 ";
    public final static String LIST_PAGE_INFO_NORMAL = "最後にご覧になった依頼ID：　";
}
