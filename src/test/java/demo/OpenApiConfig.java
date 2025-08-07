package demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {
	
	@Bean
	OpenAPI customOpenAPI() {
		// 这里可以配置OpenAPI的基本信息
		return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info().title("Demo Application API")
				.description("This is a demo application for testing purposes.").version("1.0.0"));
	}

}
