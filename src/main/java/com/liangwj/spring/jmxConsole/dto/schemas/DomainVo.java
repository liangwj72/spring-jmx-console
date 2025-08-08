package com.liangwj.spring.jmxConsole.dto.schemas;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <pre>
 * 对Mbean进行分类显示用
 * </pre>
 * 
 
 */
public class DomainVo implements Comparable<DomainVo> {
	private final String name;
	private final List<MBeanVo> beans = new LinkedList<>();

	private boolean sorted = false;

	private final int order;

	public DomainVo(String name, int order) {
		super();
		this.name = name;
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public List<MBeanVo> getBeans() {
		if (!this.sorted) {
			this.sorted = true;
			Collections.sort(this.beans);
		}
		return beans;
	}

	public void addMBean(MBeanVo vo) {
		this.beans.add(vo);
	}

	@Override
	public int compareTo(DomainVo other) {
		int res = other.order - this.order;
		if (res == 0) {
			return this.name.compareTo(other.name);
		} else {
			return res;
		}
	}

	public int getOrder() {
		return order;
	}

}
