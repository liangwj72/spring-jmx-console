package com.liangwj.spring.jmxConsole.dto.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 记录每个url的访问时间数据
 *
 * @author rock
 *
 */
@Schema(description = "记录每个url的访问时间数据")
public class UrlBean {
	@Schema(description = "访问时间")
	private final long accessTime;
	@Schema(description = "消耗的时间：毫秒")
	private final long cost;
	@Schema(description = "url")
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