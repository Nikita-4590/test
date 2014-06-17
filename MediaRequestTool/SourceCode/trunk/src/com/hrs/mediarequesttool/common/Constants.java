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
	
	public final static String[] NEXT_CONFIRMING = {"OK", "NG"};
	public final static String[] NEXT_NG = {"CONFIRMING", "DELETED"};
	
	public final static int MAX_LENGTH_ASSIGNED_PERSON = 100;
}
