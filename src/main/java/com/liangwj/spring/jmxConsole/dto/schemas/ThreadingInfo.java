package com.liangwj.spring.jmxConsole.dto.schemas;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "线程的信息")
public class ThreadingInfo implements Serializable {
	private static final long serialVersionUID = 8636078834555611575L;

	@Schema(description = "活动线程数")
	private int threadCount;

	@Schema(description = "峰值线程数")
	private int peakThreadCount;

	@Schema(description = "守护程序线程")
	private int daemonThreadCount;

	@Schema(description = "启动的线程总数")
	private long totalStartedThreadCount;

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public int getPeakThreadCount() {
		return peakThreadCount;
	}

	public void setPeakThreadCount(int peakThreadCount) {
		this.peakThreadCount = peakThreadCount;
	}

	public int getDaemonThreadCount() {
		return daemonThreadCount;
	}

	public void setDaemonThreadCount(int daemonThreadCount) {
		this.daemonThreadCount = daemonThreadCount;
	}

	public long getTotalStartedThreadCount() {
		return totalStartedThreadCount;
	}

	public void setTotalStartedThreadCount(long totalStartedThreadCount) {
		this.totalStartedThreadCount = totalStartedThreadCount;
	}

}
