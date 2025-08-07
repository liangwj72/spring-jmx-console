package demo;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import com.liangwj.spring.jmxConsole.config.JmxConsoleProperties;

import jakarta.annotation.PostConstruct;

/**
 * 测试用的 Spring Boot应用程序主启动类
 * 
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

	private static final String FOREVER_ARG = "forever"; // 持续运行参数
	private static final int AUTO_STOP_DELAY = 10000; // 自动停止延迟时间，单位毫秒

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired()
	private ServerProperties serverProp;

	@Autowired(required = false)
	private JmxConsoleProperties jmxConsoleProp;

	@Autowired
	private SwaggerUiConfigProperties swaggerUiConfigProperties;

	public static void main(String[] args) {
		final SpringApplication app = new SpringApplication(Application.class);
		app.run(args);

		// 如果没有 forever 参数，则自动退出
		if (!Arrays.asList(args).contains(FOREVER_ARG)) {
			logger.info("Auto-stop: Starting application without forever parameter, will exit after {} seconds",
					AUTO_STOP_DELAY / 1000);

			// 在新线程中执行延时关闭
			new Thread(() -> {
				try {
					Thread.sleep(AUTO_STOP_DELAY);
					logger.info("Auto-stop: Shutting down application after {} seconds", AUTO_STOP_DELAY / 1000);
					System.exit(0);
				} catch (final InterruptedException e) {
					Thread.currentThread().interrupt();
					logger.error("Auto-stop interrupted", e);
				}
			}).start();
		} else {
			logger.info("Forever mode: Application will run continuously until manually stopped");
		}
	}

	@PostConstruct
	protected void init() {
		if (this.serverProp != null) {
			logger.info("Demo Web Application listen on http://127.0.0.1:{}", this.serverProp.getPort());
		}

		if (this.jmxConsoleProp != null) {
			logger.info("JMX Console listen on http://127.0.0.1:{}", this.jmxConsoleProp.getPort());
			logger.info("Swagger UI: http://127.0.0.1:{}{}", this.jmxConsoleProp.getPort(),
					this.swaggerUiConfigProperties.getPath());
		} else {
			logger.info("JMX Console is not enabled");
		}

	}
}