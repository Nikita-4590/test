package com.hrs.mediarequesttool.common.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.hrs.mediarequesttool.common.Constants;

public abstract class Validator {
	public static boolean isNullOrEmpty(String testString) {
		return testString == null || StringUtils.strip(testString).isEmpty();
	}

	public static boolean isChecked(String testString) {
		return testString != null && StringUtils.strip(testString).equalsIgnoreCase(Constants.CHECKBOX_ON);
	}
	
	public static boolean isCheckedFlag(String testString, String flag) {
		return testString != null && StringUtils.strip(testString).equalsIgnoreCase(flag);
	}

	public static boolean checkRegex(String regex, String testString) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(testString);
		return matcher.matches();
	}

	public static boolean checkExceedLength(int maxNumber, String testString) {
		return testString == null || testString.length() > maxNumber;
	}

}
