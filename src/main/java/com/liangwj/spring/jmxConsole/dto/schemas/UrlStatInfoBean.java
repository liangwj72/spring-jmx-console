package com.liangwj.spring.jmxConsole.dto.schemas;

import java.util.LinkedList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * URL时间范围统计信息
 *
 *
 */
@Schema(description = "URL时间范围统计信息")
public class UrlStatInfoBean {
	@Schema(description = "主键（前端没有用）")
	private final int id;

	@Schema(description = "api调用次数", example = "100")
	private final long count;

	@Schema(description = "时间范围 最大值", example = "20")
	private final long timeRangeMax;

	@Schema(description = "时间范围-最小值", example = "100")
	private final long timeRangeMin;

	@Schema(description = "api url 历史记录")
	private final List<UrlBean> urlHistory = new LinkedList<>();

	public UrlStatInfoBean(int id, long timeRangeMin, long timeRangeMax, long count) {
		this.id = id;
		this.timeRangeMin = timeRangeMin;
		this.timeRangeMax = timeRangeMax;
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public long getCount() {
		return count;
	}

	public long getTimeRangeMax() {
		return timeRangeMax;
	}

	public long getTimeRangeMin() {
		return timeRangeMin;
	}

	public List<UrlBean> getUrlHistory() {
		return urlHistory;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("UrlStatInfoBean{");
		sb.append("id=").append(id).append(", ");
		sb.append("count=").append(count).append(", ");
		sb.append("timeRangeMax=").append(timeRangeMax).append(", ");
		sb.append("timeRangeMin=").append(timeRangeMin).append(", ");
		sb.append(", urlHistory=").append(urlHistory.size()).append("}");
		return sb.toString();
	}

}
