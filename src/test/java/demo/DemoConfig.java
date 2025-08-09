package demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Web配置类
 * 提供Web相关的Bean配置
 */
@Configuration
public class DemoConfig {

    /**
     * 配置RestTemplate Bean
     * 用于HTTP客户端请求
     * 
     * @return RestTemplate实例
     */
    @Bean
	RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 