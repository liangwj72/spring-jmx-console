package com.liangwj.spring.jmxConsole.dto.responses;

/**
 * <pre>
 * Invoke opt response
 * </pre>
 * 
 */
public class JwInvokeOptResponse  {

	private boolean hasReturn;
	private String returnData;

	public boolean isHasReturn() {
		return hasReturn;
	}

	public void setHasReturn(boolean hasReturn) {
		this.hasReturn = hasReturn;
	}

	public String getReturnData() {
		return returnData;
	}

	public void setReturnData(String returnData) {
		this.returnData = returnData;
	}

}
