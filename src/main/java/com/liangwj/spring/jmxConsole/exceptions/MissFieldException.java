package com.liangwj.spring.jmxConsole.exceptions;

/**
 * <pre>
 * 缺少某字段
 * </pre>
 * 
 */
public class MissFieldException extends BaseApiException {

	private static final long serialVersionUID = 1L;

	private final String message;

	public MissFieldException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
