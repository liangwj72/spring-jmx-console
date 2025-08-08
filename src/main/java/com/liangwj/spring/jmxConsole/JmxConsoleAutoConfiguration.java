package com.liangwj.spring.jmxConsole;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.liangwj.spring.jmxConsole.config.JmxConsoleProperties;
import com.liangwj.spring.jmxConsole.config.MyControllerConfiguration;
import com.liangwj.spring.jmxConsole.mainProject.MainProjectInterceptorConfiguration;
import com.liangwj.spring.jmxConsole.services.JmxConsoleServer;

/**
 * JMX Console 自动配置类
 * 
 * @see JmxConsoleServer JmxConsoleServer会去通过 MyControllerConfiguration
 * @see MyControllerConfiguration 配置mvc和扫描Controller
 */
@Configuration
@EnableConfigurationProperties(JmxConsoleProperties.class)
@ConditionalOnProperty(prefix = "spring-jmx-console", name = "port")
@ComponentScan(basePackageClasses = {
		// 扫描的包路径, 不要扫描到Controller的包，因为OpenAPI的Controller会冲突
		JmxConsoleServer.class, // Service类,
		MainProjectInterceptorConfiguration.class, // 主项目拦截器配置
})
public class JmxConsoleAutoConfiguration {

}
