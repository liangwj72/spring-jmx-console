package demo.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.client.RestTemplate;

import com.liangwj.spring.jmxConsole.config.JmxConsoleProperties;

public abstract class BaseTester {
	protected static final int AUTO_STOP_DELAY = 10000; // 自动停止延迟时间，单位毫秒

	@Autowired
	protected JmxConsoleProperties jmxConsoleProperties;

	@Autowired
	protected ServerProperties serverProp;

	@Autowired
	protected RestTemplate restTemplate;

	/**
	 * 运行测试器
	 * 
	 * @return 运行完成后是否推出整个系统
	 */
	public abstract boolean run() throws Exception;

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
