package com.liangwj.spring.jmxConsole.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.liangwj.spring.jmxConsole.controllers.HealthController;
import com.liangwj.spring.jmxConsole.controllers.RuntimeController;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * JMX Console Web配置
 */
@Configuration
@EnableWebMvc
public class MtWebConfiguration implements WebMvcConfigurer {

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI();
	}

    @Bean
	RuntimeController jmxConsoleController() {
        return new RuntimeController();
    }

	@Bean
	HealthController healthController() {
		return new HealthController();
	}

}
