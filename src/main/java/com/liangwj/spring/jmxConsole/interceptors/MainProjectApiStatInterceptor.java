package com.liangwj.spring.jmxConsole.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.liangwj.spring.jmxConsole.services.StatService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 主项目API统计拦截器
 * 用于在主项目中监控API调用，记录调用耗时和URL统计数据
 * 
 * @author rock
 */
@Component
public class MainProjectApiStatInterceptor implements HandlerInterceptor {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MainProjectApiStatInterceptor.class);

	@Autowired
	private StatService statService;

	/** 请求开始时间的属性名 */
	private static final String START_TIME_ATTRIBUTE = "main_api_start_time";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 记录请求开始时间
		request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());

		// log.debug("开始监控主项目API调用: {}", request.getRequestURI());

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		if (!(handler instanceof org.springframework.web.method.HandlerMethod)) {
			// 如果不是HandlerMethod，说明不是主项目的API调用，直接返回
			return;
		}

		final String requestURI = request.getRequestURI();

		// 获取请求开始时间
		final Object startTimeObj = request.getAttribute(START_TIME_ATTRIBUTE);
		if (startTimeObj == null) {
			log.warn("没有找到请求开始时间，跳过统计: {}", requestURI);
			return; // 没有开始时间，跳过统计
		}

		final long startTime = (Long) startTimeObj;
		final long endTime = System.currentTimeMillis();
		final long costTime = endTime - startTime;

		// 提取纯URL路径（不包含查询参数）
		final String cleanUrl = extractCleanUrl(requestURI);

		// 调用统计服务记录API调用数据
		statService.onApiFinish(cleanUrl, costTime);

		log.debug("完成主项目API调用统计: url={}, costTime={}ms", cleanUrl, costTime);
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
