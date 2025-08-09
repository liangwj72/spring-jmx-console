package demo.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import com.liangwj.spring.jmxConsole.config.JmxConsoleProperties;

public abstract class BaseTester {
	protected static final int AUTO_STOP_DELAY = 10000; // 自动停止延迟时间，单位毫秒

	@Autowired
	protected JmxConsoleProperties jmxConsoleProperties;

	@Autowired
	protected ServerProperties serverProp;

	/** 当系统初始化完成后 */
	public abstract void run();

	/**
	 * 获取主项目的完整URL
	 * 
	 * @param path 请求路径
	 * @return 完整的URL
	 */
	protected String getFullUrlMainProject(String path) {
		return String.format("http://localhost:%d/%s", serverProp.getPort(), path);
	}

	/**
	 * 获取JMX控制台项目的完整URL
	 * 
	 * @param path 请求路径
	 * @return 完整的URL
	 */
	protected String getFullUrlJmxConsoleProject(String path) {
		return String.format("http://localhost:%d/%s", jmxConsoleProperties.getPort(), path);
	}
}
