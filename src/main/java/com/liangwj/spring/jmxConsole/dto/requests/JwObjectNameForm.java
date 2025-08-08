package com.liangwj.spring.jmxConsole.dto.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <pre>
 * MBean操作的form的基类
 * </pre>
 * 
 */
public class JwObjectNameForm {

	@NotNull(message = "objectName不能为空")
	@Size(min = 1, message = "objectName不能为空")
	private String objectName;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
}
