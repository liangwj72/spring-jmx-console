package com.liangwj.spring.jmxConsole.exceptions;

/**
 * 调用jmx的过程发送了错误
 */
public class MyJmException extends BaseApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyJmException(Exception ex) {
		super(ex);
	}
}
