package com.liangwj.spring.jmxConsole.dto.schemas;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 运行状态一个时间点的值
 */
@Schema(description = "一个时间点的运行状态")
public class RuntimeInfoBean implements Serializable {

	private static final long serialVersionUID = -7509975362349926582L;

	@Schema(description = "记录时间点")
	private long recordTime;

	@Schema(description = "http请求次数")
	private long actionCount;

	@Schema(description = "jvm的负载")
	private double processCpuLoad;

	@Schema(description = "发包次数")
	private long sendPacketCount;

	@Schema(description = "发包带宽")
	private long sendPacketPayload;

	@Schema(description = "内存使用信息")
	private OsMemoryInfoBean memory;

	@Schema(description = "线程数量")
	private int threadCount;

	/** 是否当前秒 */
	public boolean curTimeSec() {
		long now = System.currentTimeMillis() / 1000 * 1000;
		return now == this.recordTime;
	}
}
