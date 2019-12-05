package com.k66.cxzt.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class EncryptUtil {
	
	private static final char[] HEX_DIGITS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' };
	private static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

	private EncryptUtil() {
	}

	public static final String hex(String s,String type) {
		if(isEmpty(s)){
			return "";
		}
		try {
			byte[] strTemp = s.getBytes("utf-8");
			MessageDigest mdTemp = MessageDigest.getInstance(type);
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xF];
				str[k++] = HEX_DIGITS[byte0 & 0xF];
			}
			return new String(str);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return "";
	}

	public static String encoder(String str, String charset){
		try {
			return URLEncoder.encode(str, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	public static String generateRandomPassword(int length){
		Random random = new Random();
		String str = "";
		for(int i = 0 ; i < length ; i++){
			int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
			str += (char)(random.nextInt(26) + temp);
		}
		return str;
	}

	public static void main(String[] args) {
//		System.out.println(EncryptUtil.hex("7c4a8d09ca3762af61e59520943dc26494f8941b" , "sha1"));
		System.out.println(EncryptUtil.hex(EncryptUtil.hex("123456" , "sha1") , "md5"));
	}
}
