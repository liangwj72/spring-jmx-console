package com.liangwj.spring.jmxConsole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.liangwj.spring.jmxConsole.config.JmxConsoleProperties;
import com.liangwj.spring.jmxConsole.services.MyServer;

/**
 * JMX Console 自动配置类
 */
@Configuration
@EnableConfigurationProperties(JmxConsoleProperties.class)
@ConditionalOnProperty(prefix = "spring-jmx-console", name = "port")
public class JmxConsoleAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(JmxConsoleAutoConfiguration.class);

	@Bean
	MyServer myServer(JmxConsoleProperties properties) {
		logger.info("Creating JMX Console Server with port: {}", properties.getPort());
		return new MyServer(properties);
	}
}
