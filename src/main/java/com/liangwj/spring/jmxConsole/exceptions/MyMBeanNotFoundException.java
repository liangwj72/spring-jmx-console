package com.liangwj.spring.jmxConsole.exceptions;

/**
 * 无法根据ObjectName找到对应的MBean
 */
public class MyMBeanNotFoundException extends BaseApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String name;

	public MyMBeanNotFoundException(String name) {
		this.name = name;
	}

	@Override
	public String getMessage() {
		return String.format("找不到MBean:%s", this.name);
	}

}
