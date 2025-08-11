package com.liangwj.spring.jmxConsole.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.liangwj.spring.jmxConsole.config.JmxConsoleProperties;
import com.liangwj.spring.jmxConsole.config.MyControllerConfiguration;

import jakarta.servlet.ServletRegistration;

/**
 * JMX Console 独立服务器
 */
@Service
public class JmxConsoleServer implements InitializingBean, DisposableBean, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(JmxConsoleServer.class);

	private final JmxConsoleProperties properties;
	private AnnotationConfigWebApplicationContext webContext;
	private ApplicationContext parentContext;
	private WebServer webServer;

	private final Object startSingal = new Object(); // 启动完成的信号
	private boolean started = false;

	public JmxConsoleServer(JmxConsoleProperties properties) {
		this.properties = properties;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.parentContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		startJmxConsoleServer();
	}

	/**
	 * 等待JMX Console服务器启动完成
	 */
	public void waitForStart() {
		if (started) {
			return; // 已经启动完成
		}

		synchronized (startSingal) {
			try {
				startSingal.wait(); // 等待启动完成的信号
				logger.info("JMX Console Server 启动完成，退出等待");
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // 恢复中断状态
				logger.error("Interrupted while waiting for JMX Console Server to start", e);
			}
		}
	}

	private void startJmxConsoleServer() {
		try {
			logger.info("==== Starting JMX Console Server on port: {} ====", properties.getPort());

			// 创建嵌入式Tomcat服务器
			final TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
			factory.setPort(properties.getPort());

			// 配置DispatcherServlet
			factory.addInitializers(servletContext -> {
				// 创建独立的Web应用上下文
				webContext = new AnnotationConfigWebApplicationContext();
				webContext.setParent(parentContext);
				webContext.setServletContext(servletContext);
				// 注册Web配置（包含手动的OpenAPI端点）
				webContext.register(MyControllerConfiguration.class);
				webContext.refresh();

				final DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext);
				final ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcherServlet",
						dispatcherServlet);
				registration.addMapping("/");
				registration.setLoadOnStartup(1);
			});

			// 启动服务器
			webServer = factory.getWebServer();
			webServer.start();

			logger.info("\tSwagger UI: http://127.0.0.1:{}/swagger-ui/index.html", properties.getPort());

			this.started = true;

			synchronized (startSingal) {
				startSingal.notifyAll(); // 通知等待的线程，服务器已启动完成
			}

		} catch (final Exception e) {
			logger.error("Failed to start JMX Console Server", e);
			throw new RuntimeException("Failed to start JMX Console Server", e);
		}
	}

	@Override
	public void destroy() throws Exception {
		if (webServer != null) {
			logger.info("Shutting down JMX Console Server");
			webServer.stop();
		}
		if (webContext != null) {
			webContext.close();
		}
	}
}
