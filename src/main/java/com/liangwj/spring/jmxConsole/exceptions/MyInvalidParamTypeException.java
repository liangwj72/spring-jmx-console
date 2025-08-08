package com.liangwj.spring.jmxConsole.exceptions;

/**
 * 参数类型不可从String转换过来
 */
public class MyInvalidParamTypeException extends BaseApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String type;

	public MyInvalidParamTypeException(String type) {
		this.type = type;
	}

	@Override
	public String getMessage() {
		return String.format("参数类型错误: %s", type);
	}

}
