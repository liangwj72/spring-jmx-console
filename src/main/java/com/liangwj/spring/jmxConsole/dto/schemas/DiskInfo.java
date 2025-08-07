package com.liangwj.spring.jmxConsole.dto.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 磁盘信息
 */
@Schema(description = "磁盘信息")
public class DiskInfo {

	@Schema(description = "空闲空间")
	private long freeSpace;
	@Schema(description = "总空间")
	private long totalSpace;
	@Schema(description = "可使用的空间")
	private long usableSpace;

	public long getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(long freeSpace) {
		this.freeSpace = freeSpace;
	}

	public long getTotalSpace() {
		return totalSpace;
	}

	public void setTotalSpace(long totalSpace) {
		this.totalSpace = totalSpace;
	}

	public long getUsableSpace() {
		return usableSpace;
	}

	public void setUsableSpace(long usableSpace) {
		this.usableSpace = usableSpace;
	}

}
