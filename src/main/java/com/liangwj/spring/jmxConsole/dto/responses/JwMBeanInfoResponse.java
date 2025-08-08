package com.liangwj.spring.jmxConsole.dto.responses;

import com.liangwj.spring.jmxConsole.dto.schemas.MBeanVo;

/**
 * <pre>
 * 获取一个Mbean信息的响应
 * </pre>
 * 
 */
public class JwMBeanInfoResponse{

	private MBeanVo info;

	public MBeanVo getInfo() {
		return info;
	}

	public void setInfo(MBeanVo info) {
		this.info = info;
	}

}
