package com.liangwj.spring.jmxConsole.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceUtils {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExecutorServiceUtils.class);

	public static void shutdownExecutorService(String name, ExecutorService executorService) {
		if (executorService == null) {
			return;
		}

		executorService.shutdown();
		try {
			// 5分钟如果不能停止就强行停止
			if (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
				executorService.shutdownNow();
				if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
					log.error(name + " shutdown 失败");
				}
			}
		} catch (final InterruptedException e) {
			log.error("ExecutorService shutdown时出错", e);
			executorService.shutdownNow();
		}
		log.info("成功停止线程池: “{}” ", name);
	}
}
