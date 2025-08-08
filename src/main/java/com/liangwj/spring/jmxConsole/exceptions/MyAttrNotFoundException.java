package com.liangwj.spring.jmxConsole.exceptions;


/**
 * <pre>
 * 要修改属性时，属性的值找不到
 */
public class MyAttrNotFoundException extends BaseApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String attrName;

	public MyAttrNotFoundException(String attrName) {
		this.attrName = attrName;
	}

	@Override
	public String getMessage() {
		return String.format("找不到属性%s", this.attrName);
	}

}
