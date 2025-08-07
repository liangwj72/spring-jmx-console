package com.liangwj.spring.jmxConsole.dto.responses;

import java.util.List;

import com.liangwj.spring.jmxConsole.dto.schemas.DiskInfo;
import com.liangwj.spring.jmxConsole.dto.schemas.RuntimeInfoBean;
import com.liangwj.spring.jmxConsole.dto.schemas.UrlStatInfoBean;

import io.swagger.v3.oas.annotations.media.Schema;

/** 运行状态的响应 */
@Schema(description = "运行状态的响应")
public class RuntimeHistoryResponse {

	@Schema(description = "磁盘信息")
	private DiskInfo diskInfo;

	@Schema(description = "所有时间范围状态")
	private List<UrlStatInfoBean> uriStat;

	@Schema(description = "图表数据")
	private List<RuntimeInfoBean> list;

	public DiskInfo getDiskInfo() {
		return diskInfo;
	}

	public void setDiskInfo(DiskInfo diskInfo) {
		this.diskInfo = diskInfo;
	}

	public List<UrlStatInfoBean> getUriStat() {
		return uriStat;
	}

	public void setUriStat(List<UrlStatInfoBean> uriStat) {
		this.uriStat = uriStat;
	}

	public List<RuntimeInfoBean> getList() {
		return list;
	}

	public void setList(List<RuntimeInfoBean> list) {
		this.list = list;
	}

}
