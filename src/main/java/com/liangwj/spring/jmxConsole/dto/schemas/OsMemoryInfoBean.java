package com.liangwj.spring.jmxConsole.dto.schemas;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JVM内存信息")
public class OsMemoryInfoBean implements Serializable {

	private static final long serialVersionUID = 6178360881892025876L;

	/** 已经分配不代表已经使用 */
	@Schema(description = "已分配内存")
	private long totalMemory;

	/** 还可分配的内存 */
	@Schema(description = "还可分配的内存")
	private long freeMemory;

	/** 已经使用的内存 */
	@Schema(description = "已经使用的内存")
	private long usedMemory;

	/** 最大可分配内存 */
	@Schema(description = "最大可分配内存")
	private long maxMemory;

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

}
