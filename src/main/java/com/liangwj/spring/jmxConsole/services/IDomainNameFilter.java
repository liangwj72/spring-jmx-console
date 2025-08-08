package com.liangwj.spring.jmxConsole.services;

/**
 * <pre>
 * MBean Domain的过滤器，有时我们并不希望所有的Domain都显示出来
 * </pre>
 * 
 */
public interface IDomainNameFilter {

	boolean show(String domainName);

}
