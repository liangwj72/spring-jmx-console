package com.liangwj.spring.jmxConsole.exceptions;

/**
 * 无法根据optName找到对应的MBean中的操作
 */
public class MyOperationNotFoundException extends BaseApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String operationsInfo;

	public MyOperationNotFoundException(String operationsInfo) {
		this.operationsInfo = operationsInfo;
	}

	@Override
	public String getMessage() {

		return String.format("Operation %s not found", this.operationsInfo);
	}

}
