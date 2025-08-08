package com.liangwj.spring.jmxConsole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.liangwj.spring.jmxConsole.config.JmxConsoleProperties;
import com.liangwj.spring.jmxConsole.config.MainProjectInterceptorConfiguration;
import com.liangwj.spring.jmxConsole.interceptors.MainProjectApiStatInterceptor;
import com.liangwj.spring.jmxConsole.services.StatService;

/**
 * JMX Console 自动配置类
 */
@Configuration
@EnableConfigurationProperties(JmxConsoleProperties.class)
@ConditionalOnProperty(prefix = "spring-jmx-console", name = "port")
@Import(MainProjectInterceptorConfiguration.class)
public class JmxConsoleAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(JmxConsoleAutoConfiguration.class);

	@Bean
	JmxConsoleServer myServer(JmxConsoleProperties properties) {
		logger.info("Creating JMX Console Server with port: {}", properties.getPort());
		return new JmxConsoleServer(properties);
	}

	@Bean
	StatService statService() {
		logger.info("Creating StatService for main project API monitoring");
		return new StatService();
	}

	@Bean
	MainProjectApiStatInterceptor mainProjectApiStatInterceptor() {
		logger.info("Creating MainProjectApiStatInterceptor for main project API monitoring");
		return new MainProjectApiStatInterceptor();
	}
}
