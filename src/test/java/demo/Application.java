package demo;

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

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private ServerProperties serverProp;

	@Autowired(required = false)
	private JmxConsoleProperties jmxConsoleProp;

	@Autowired(required = false)
	private SwaggerUiConfigProperties swaggerUiConfigProperties;

	public static void main(String[] args) {
		final SpringApplication app = new SpringApplication(Application.class);
		app.run(args);
	}

	@PostConstruct
	protected void init() {
		logger.info("==== Web Application Start ====");
		logger.info("\tWeb Application listen on:  http://127.0.0.1:{}", this.serverProp.getPort());
		if (this.swaggerUiConfigProperties != null) {
			logger.info("\tWeb Application Swagger UI: http://127.0.0.1:{}{}", this.serverProp.getPort(),
					this.swaggerUiConfigProperties.getPath());
		} else {
			logger.info("\tWeb Application Swagger UI: not enabled");
		}

		if (this.jmxConsoleProp != null) {
			logger.info("JMX Console is enabled");
		} else {
			logger.info("JMX Console is not enabled");
		}
	}
}