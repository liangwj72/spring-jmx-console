package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.liangwj.spring.jmxConsole.services.JmxConsoleServer;

@Component
public class ServerStartedListener implements ApplicationListener<WebServerInitializedEvent> {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ServerStartedListener.class);

	@Autowired
	private TesterRunner testerRunner;

	@Autowired
	private JmxConsoleServer jmxConsoleServer;

	@Override
	public void onApplicationEvent(WebServerInitializedEvent event) {
		int port = event.getWebServer().getPort();
		log.debug("HTTP 服务器已启动，监听端口: {}", port);

		// 等待 JMX 控制台服务器启动
		this.jmxConsoleServer.waitForStart();

		// 启动测试运行器
		this.testerRunner.runTests();
	}

}