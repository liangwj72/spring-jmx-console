package com.liangwj.spring.jmxConsole.utils;

import org.springframework.util.StringUtils;

import com.liangwj.spring.jmxConsole.exceptions.MissFieldException;

/**
 * <pre>
 * 用于抛错的工具
 * </pre>
 */
public class ExceptionUtil {


	public static void notNull(Object obj, String message) throws MissFieldException {
		if (obj == null) {
			throw new MissFieldException(message);
		}
	}

	public static void hasText(String str, String message) throws MissFieldException {
		if (!StringUtils.hasText(str)) {
			throw new MissFieldException(message);
		}
	}

	public static void notZero(int value, String message) throws MissFieldException {
		if (value <= 0) {
			throw new MissFieldException(message);
		}
	}


}
