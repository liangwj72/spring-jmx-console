package com.liangwj.spring.jmxConsole.exceptions;

/**
 *  ObjectName 格式错误
 */
public class MyMalformedObjectNameException extends BaseApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String nameStr;

	public MyMalformedObjectNameException(String nameStr) {
		this.nameStr = nameStr;
	}

	@Override
	public String getMessage() {
		return String.format("ObjectName格式错误: %s", this.nameStr);
	}

}
