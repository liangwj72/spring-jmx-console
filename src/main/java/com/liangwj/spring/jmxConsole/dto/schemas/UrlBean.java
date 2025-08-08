package com.liangwj.spring.jmxConsole.dto.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "记录每个api 的访问时间数据")
public class UrlBean {
	@Schema(description = "api 访问时间，UNIX 时间戳", example = "1754648162967")
	private final long accessTime;

	@Schema(description = "api 消耗的时间：毫秒", example = "123")
	private final long cost;

	@Schema(description = "api 的 url", example = "/api/v1/resource")
	private final String url;

	public UrlBean(String url, long cost) {
		super();
		this.url = url;
		this.cost = cost;
		this.accessTime = System.currentTimeMillis();
	}

	public long getAccessTime() {
		return accessTime;
	}

	public long getCost() {
		return cost;
	}

	public String getUrl() {
		return url;
	}

}