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

}
