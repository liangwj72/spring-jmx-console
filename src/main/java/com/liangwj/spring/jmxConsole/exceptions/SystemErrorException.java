package com.liangwj.spring.jmxConsole.exceptions;

/**
 * <pre>
 * 其他错误
 * </pre>
 * 
 *  2016年7月2日
 */
public class SystemErrorException extends BaseApiException {

	public SystemErrorException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "系统发生内部错误";
	}

}
