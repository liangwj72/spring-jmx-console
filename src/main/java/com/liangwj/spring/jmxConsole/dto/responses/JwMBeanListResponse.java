package com.liangwj.spring.jmxConsole.dto.responses;

import java.util.LinkedList;
import java.util.List;

import com.liangwj.spring.jmxConsole.dto.schemas.DomainVo;

/**
 * <pre>
 * 返回 所有的mbean
 * </pre>
 * 
 * @author rock 2017年4月25日
 */
public class JwMBeanListResponse  {

	private List<DomainVo> list = new LinkedList<>();

	public List<DomainVo> getList() {
		return list;
	}

	public void setList(List<DomainVo> list) {
		this.list = list;
	}

}
