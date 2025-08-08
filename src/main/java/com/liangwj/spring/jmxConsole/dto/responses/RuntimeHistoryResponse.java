package com.liangwj.spring.jmxConsole.dto.responses;

import java.util.List;

import com.liangwj.spring.jmxConsole.dto.schemas.DiskInfoBean;
import com.liangwj.spring.jmxConsole.dto.schemas.RuntimeInfoBean;
import com.liangwj.spring.jmxConsole.dto.schemas.UrlStatInfoBean;

import io.swagger.v3.oas.annotations.media.Schema;

/** 运行状态的响应 */
@Schema(description = "运行状态的响应")
public class RuntimeHistoryResponse {

	@Schema(description = "磁盘信息")
	private DiskInfoBean diskInfo;

	@Schema(description = "api的url统计")
	private List<UrlStatInfoBean> uriStat = new java.util.ArrayList<>();

	@Schema(description = "运行状态的图表数据")
	private List<RuntimeInfoBean> runtimes = new java.util.ArrayList<>();

	public DiskInfoBean getDiskInfo() {
		return diskInfo;
	}

	public void setDiskInfo(DiskInfoBean diskInfo) {
		this.diskInfo = diskInfo;
	}

	public List<UrlStatInfoBean> getUriStat() {
		return uriStat;
	}

	public void setUriStat(List<UrlStatInfoBean> uriStat) {
		this.uriStat = uriStat;
	}

	public List<RuntimeInfoBean> getRuntimes() {
		return runtimes;
	}

	public void setRuntimes(List<RuntimeInfoBean> runtimes) {
		this.runtimes = runtimes;
	}

}
