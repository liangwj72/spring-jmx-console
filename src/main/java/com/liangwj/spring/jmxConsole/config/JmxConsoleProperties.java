package com.liangwj.spring.jmxConsole.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JMX控制台属性类
 */
@ConfigurationProperties("spring-jmx-console")
public class JmxConsoleProperties {


	private int port = 60080; // 默认端口

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
