package com.liangwj.spring.jmxConsole.dto.responses;

import com.liangwj.spring.jmxConsole.dto.schemas.UrlStatInfoBean;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * URL统计
 *
 */
@Schema(description = "URL统计")
public class UrlStatResponse {
	@Schema(description = "所有事件范围状态")
	private UrlStatInfoBean stat;

	public UrlStatInfoBean getStat() {
		return stat;
	}

	public void setStat(UrlStatInfoBean stat) {
		this.stat = stat;
	}
}
