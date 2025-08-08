package com.liangwj.spring.jmxConsole.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.liangwj.spring.jmxConsole.controllers.HealthController;

/**
 * JMX Console Web配置: 扫描所有的Controller类
 */
@Configuration
@ComponentScan(basePackageClasses = {
		// 扫描的包路径
		HealthController.class, // Controller类
})
@EnableWebMvc // 必须在这里启用Spring MVC
public class MyControllerConfiguration {

}