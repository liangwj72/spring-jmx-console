package com.liangwj.spring.jmxConsole.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.liangwj.spring.jmxConsole.controllers.HealthController;
import com.liangwj.spring.jmxConsole.services.StatService;

/**
 * JMX Console Web配置
 * 不创建OpenAPI相关的Bean，通过手动实现的OpenApiController提供API文档
 */
@Configuration
@ComponentScan(basePackageClasses = {
		// 扫描的包路径
		HealthController.class, // Controller类
		StatService.class, // 服务类
})
@EnableWebMvc
public class MyWebConfiguration implements WebMvcConfigurer {

}