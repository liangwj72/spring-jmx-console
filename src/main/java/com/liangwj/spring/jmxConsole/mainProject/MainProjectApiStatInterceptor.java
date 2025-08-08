package com.liangwj.spring.jmxConsole.mainProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.liangwj.spring.jmxConsole.JmxConsoleAutoConfiguration;
import com.liangwj.spring.jmxConsole.services.StatService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 主项目API统计拦截器 用于在主项目中监控API调用，记录调用耗时和URL统计数据
 * 
 */
@Component
public class MainProjectApiStatInterceptor implements HandlerInterceptor {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MainProjectApiStatInterceptor.class);

	@Autowired
	private StatService statService;

	/** 请求开始时间 */
	private final ThreadLocal<Long> apiStartTime = new ThreadLocal<Long>();

	/** JMX Console的包名 */
	private static final String JMX_CONSOLE_PACKAGE = JmxConsoleAutoConfiguration.class.getPackage().getName();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			// 如果是HandlerMethod，说明是API调用

			// 找到调用的方法
			final var target = (org.springframework.web.method.HandlerMethod) handler;
			final var targetClass = target.getBeanType();

			// 如果目标方法的类的前缀是JMX Console的包名，则跳过统计
			if (!targetClass.getName().startsWith(JMX_CONSOLE_PACKAGE)) {
				// 如果调用的类不是 JMX Console的包名，则表示是主项目API调用，记录请求开始时间
				apiStartTime.set(System.currentTimeMillis());
				
				return true;
			} else {
				// 如果目标方法的类的前缀是JMX Console的包名，则跳过统计
				log.debug("跳过JMX Console API调用: {}", request.getRequestURI());
			}
		} else {
			// 只统计真正的Controller方法调用
			log.debug("跳过非Controller方法: {}", request.getRequestURI());
		}

		// 如果handler不是HandlerMethod，则表示不是API调用，设置请求开始时间为0
		apiStartTime.set(0L);

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		final Long startTime = apiStartTime.get();
		if (startTime == 0L) {
			// 如果请求开始时间为0，说明不是主项目的API调用，直接返回
			return;
		}

		// 计算请求耗时
		final long endTime = System.currentTimeMillis();
		final long costTime = endTime - startTime;

		// 提取纯URL路径（不包含查询参数）
		final String cleanUrl = extractCleanUrl(request.getRequestURI());

		// 调用统计服务记录API调用数据
		statService.onApiFinish(cleanUrl, costTime);

//		log.debug("完成主项目API调用统计: url={}, costTime={}ms", cleanUrl, costTime);
	}

	/**
	 * 提取纯URL路径，去除查询参数
	 */
	private String extractCleanUrl(String requestURI) {
		final int queryIndex = requestURI.indexOf('?');
		if (queryIndex != -1) {
			return requestURI.substring(0, queryIndex);
		}
		return requestURI;
	}
}
