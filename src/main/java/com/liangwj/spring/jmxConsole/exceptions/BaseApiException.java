package com.liangwj.spring.jmxConsole.exceptions;

public class BaseApiException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BaseApiException(Exception ex) {
		super(ex);
	}

	public BaseApiException() {
	}

}
