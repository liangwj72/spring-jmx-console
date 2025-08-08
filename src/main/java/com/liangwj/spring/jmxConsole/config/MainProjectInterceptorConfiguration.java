package com.liangwj.spring.jmxConsole.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.liangwj.spring.jmxConsole.interceptors.MainProjectApiStatInterceptor;

/**
 * 主项目拦截器配置
 * 这个配置会在主项目中生效，用于拦截主项目的API调用
 * 
 * @author rock
 */
@Configuration
public class MainProjectInterceptorConfiguration implements WebMvcConfigurer {

	@Autowired
	private MainProjectApiStatInterceptor mainProjectApiStatInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册API统计拦截器，只拦截主项目的API
		registry.addInterceptor(mainProjectApiStatInterceptor)
				.addPathPatterns("/**") // 拦截所有路径
		;
	}
}
