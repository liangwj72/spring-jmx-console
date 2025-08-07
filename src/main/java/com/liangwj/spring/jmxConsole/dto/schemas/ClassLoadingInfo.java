package com.liangwj.spring.jmxConsole.dto.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "类加载的信息")
public class ClassLoadingInfo {
	@Schema(description = "当前加载的类的总数")
	private int loadedClassCount;
	@Schema(description = "总共加载的类的总数")
	private long totalLoadedClassCount;
	@Schema(description = "已经卸载的类的总数")
	private long unloadedClassCount;

	public int getLoadedClassCount() {
		return loadedClassCount;
	}

	public void setLoadedClassCount(int loadedClassCount) {
		this.loadedClassCount = loadedClassCount;
	}

	public long getTotalLoadedClassCount() {
		return totalLoadedClassCount;
	}

	public void setTotalLoadedClassCount(long totalLoadedClassCount) {
		this.totalLoadedClassCount = totalLoadedClassCount;
	}

	public long getUnloadedClassCount() {
		return unloadedClassCount;
	}

	public void setUnloadedClassCount(long unloadedClassCount) {
		this.unloadedClassCount = unloadedClassCount;
	}

}
