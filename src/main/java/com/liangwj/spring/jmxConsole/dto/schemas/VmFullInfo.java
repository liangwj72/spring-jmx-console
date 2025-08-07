package com.liangwj.spring.jmxConsole.dto.schemas;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "虚拟机的完整信息，包括类路径、引导类路径和库路径等")
public class VmFullInfo extends VmInfo {

	private static final long serialVersionUID = 350357583922719983L;

	@Schema(description = "类路径")
	private java.lang.String classPath;
	@Schema(description = "引导类路径")
	private java.lang.String bootClassPath;
	@Schema(description = "库路径")
	private java.lang.String libraryPath;

	public java.lang.String getClassPath() {
		return classPath;
	}

	public void setClassPath(java.lang.String classPath) {
		this.classPath = classPath;
	}

	public java.lang.String getBootClassPath() {
		return bootClassPath;
	}

	public void setBootClassPath(java.lang.String bootClassPath) {
		this.bootClassPath = bootClassPath;
	}

	public java.lang.String getLibraryPath() {
		return libraryPath;
	}

	public void setLibraryPath(java.lang.String libraryPath) {
		this.libraryPath = libraryPath;
	}

}
