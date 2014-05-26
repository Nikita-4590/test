package com.hrs.mediarequesttool.common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncrypt {
	public static String hashPassword(String password) {
		String hashedPassword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashedPassword = hash.toString(16);
		} catch (NoSuchAlgorithmException nsae) {
			// ignore
		}
		return hashedPassword;
	}
}
