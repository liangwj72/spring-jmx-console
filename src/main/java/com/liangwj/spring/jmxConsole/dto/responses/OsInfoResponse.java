package com.liangwj.spring.jmxConsole.dto.responses;

import org.springframework.boot.info.OsInfo;

import com.liangwj.spring.jmxConsole.dto.schemas.ClassLoadingInfo;
import com.liangwj.spring.jmxConsole.dto.schemas.ThreadingInfo;
import com.liangwj.spring.jmxConsole.dto.schemas.VmInfo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 服务器硬件和虚拟机信息
 */
@Schema(description = "服务器硬件和虚拟机信息")
public class OsInfoResponse {

	@Schema(description = "操作系统信息")
	private OsInfo os;

	@Schema(description = "类加载信息")
	private ClassLoadingInfo classLoading;

	@Schema(description = "线程信息")
	private ThreadingInfo threading;

	@Schema(description = "虚拟机信息")
	private VmInfo vm;

	public OsInfo getOs() {
		return os;
	}

	public void setOs(OsInfo os) {
		this.os = os;
	}

	public ClassLoadingInfo getClassLoading() {
		return classLoading;
	}

	public void setClassLoading(ClassLoadingInfo classLoading) {
		this.classLoading = classLoading;
	}

	public ThreadingInfo getThreading() {
		return threading;
	}

	public void setThreading(ThreadingInfo threading) {
		this.threading = threading;
	}

	public VmInfo getVm() {
		return vm;
	}

	public void setVm(VmInfo vm) {
		this.vm = vm;
	}

}
