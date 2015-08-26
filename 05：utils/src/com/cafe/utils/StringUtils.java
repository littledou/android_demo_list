package com.cafe.utils;

/**
 * 
 * String工具类
 */
public final class StringUtils {


	public final static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public final static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

	public final static boolean isEmpty(Object o) {
		return o == null || isEmpty(String.valueOf(o));
	}

	public final static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}


}
