package com.liangwj.spring.jmxConsole.exceptions;

import java.io.Serializable;

/**
 * 找不到数据
 */
public class IdNotFoundException extends BaseApiException {

	private static final long serialVersionUID = -7477676262967808798L;

	private String errorMsg;
	private Serializable id;

	public IdNotFoundException(String errorMsg, Serializable id) {
		this.errorMsg = errorMsg;
		this.id = id;
	}

	@Override
	public String getMessage() {
		return String.format("%s. Id: %s not found!", errorMsg, String.valueOf(id));
	}

}
