package com.liangwj.spring.jmxConsole.utils;

import java.io.File;

import com.liangwj.spring.jmxConsole.dto.schemas.DiskInfoBean;
import com.liangwj.spring.jmxConsole.dto.schemas.OsMemoryInfoBean;

/**
 * 操作系统系统工具
 */
public class OsUtil {

	/**
	 * 获得内存使用情况
	 */
	public static OsMemoryInfoBean getMemoryInfo() {

		final long totalMemory = Runtime.getRuntime().totalMemory(); // 已经分配的内存总数
		final long freeMemory = Runtime.getRuntime().freeMemory(); // 可分配的内存总数
		final long maxMemory = Runtime.getRuntime().maxMemory(); // 可分配的最大内存数

		final OsMemoryInfoBean bean = new OsMemoryInfoBean();
		bean.setFreeMemory(freeMemory);
		bean.setMaxMemory(maxMemory);
		bean.setTotalMemory(totalMemory);
		bean.setUsedMemory(totalMemory - freeMemory);
		return bean;
	}

	/**
	 * 获取硬盘信息
	 */
	public static DiskInfoBean getDiskInfo() {
		final File file = new File(".");

		final DiskInfoBean bean = new DiskInfoBean();
		bean.setFreeSpace(file.getFreeSpace());
		bean.setTotalSpace(file.getTotalSpace());
		bean.setUsableSpace(file.getUsableSpace());

		return bean;
	}

}
