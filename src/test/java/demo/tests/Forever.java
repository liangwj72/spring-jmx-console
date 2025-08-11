package demo.tests;

import org.springframework.stereotype.Component;

@Component
public class Forever extends BaseTester {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Forever.class);

	@Override
	public boolean run() {
		// 永久运行，不做任何操作
		log.info("Forever 测试模式: 应用会持续运行下去，不会自动停止。");

		this.testOpenApiMainProject();

		this.testOpenApiJmxConsoleProject();

		return false; // 返回 false 表示不会退出程序
	}

	private void testOpenApiMainProject() {
		var url = getFullUrlMainProject("v3/api-docs");
		String response = this.restTemplate.getForObject(url, String.class);

		assert response != null && !response.isEmpty() : "OpenAPI接口响应为空或无效";

		log.debug("测试主项目的OpenAPI接口 - 成功");
	}

	private void testOpenApiJmxConsoleProject() {
		var url = this.getFullUrlJmxConsoleProject("v3/api-docs");
		String response = this.restTemplate.getForObject(url, String.class);

		assert response != null && !response.isEmpty() : "OpenAPI接口响应为空或无效";

		log.debug("测试JmxConsole 的OpenAPI接口 - 成功");
	}

}
