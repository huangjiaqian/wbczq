package com.huangjq.wbssq.commons;

public class StringUtils {
	public static boolean isBlank(String str){
		if(str == null || str.equals("")){
			return true;
		}
		return false;
	}
	public static String null2String(String str){
		return isBlank(str)?"":str;
	}
	public static String null2String(String str,String src){
		return str == null?src:str;
	}
	public static String appendStr(Object... objects){
		StringBuffer sb = new StringBuffer();
		for (Object object : objects) {
			sb.append(object);
		}
		return sb.toString();
	}
}
