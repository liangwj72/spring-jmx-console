package com.liangwj.spring.jmxConsole.dto.schemas;

import javax.management.MBeanOperationInfo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "描述一个mbean的opteration信息")
public class MBeanOperationInfoVo {
	private final MBeanOperationInfo info;

	public MBeanOperationInfoVo(MBeanOperationInfo info) {
		super();
		this.info = info;
	}

	@Schema(description = "返回类型")
	public String getReturnType() {
		return this.info.getReturnType();
	}

	@Schema(description = "说明")
	public String getDescription() {
		return this.info.getDescription();
	}

	@Schema(description = "名字")
	public String getName() {
		return this.info.getName();
	}

}
