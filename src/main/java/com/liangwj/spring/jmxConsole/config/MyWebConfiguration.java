package com.liangwj.spring.jmxConsole.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.liangwj.spring.jmxConsole.controllers.HealthController;

/**
 * JMX Console Web配置
 */
@Configuration
@ComponentScan(basePackageClasses = HealthController.class)
@EnableWebMvc
public class MyWebConfiguration implements WebMvcConfigurer {

}
