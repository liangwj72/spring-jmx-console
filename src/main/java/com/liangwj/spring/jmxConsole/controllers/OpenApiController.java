package com.liangwj.spring.jmxConsole.controllers;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.liangwj.spring.jmxConsole.config.JmxConsoleProperties;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 手动实现的OpenAPI端点 - 只扫描JMX Console自己的Controller
 */
@RestController
public class OpenApiController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OpenApiController.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private JmxConsoleProperties properties;

	private static final String OPENAPI_PATH = "/v3/api-docs";
	private static final String SWAGGER_CONFIG_PATH = "/v3/api-docs/swagger-config";

	// JMX Console Controller包名
	private static final String CONTROLLER_PACKAGE = HealthController.class.getPackageName();

	@GetMapping(value = OPENAPI_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getOpenApiSpec() {
		final Map<String, Object> openApi = new LinkedHashMap<>();

		// OpenAPI 基本信息
		openApi.put("openapi", "3.0.1");

		final Map<String, Object> info = new LinkedHashMap<>();
		info.put("title", "JMX Console API");
		info.put("description", "JMX Console管理接口 - 独立运行端口");
		info.put("version", "1.0.0");
		openApi.put("info", info);

		// 扫描当前Web上下文中的Controller
		final Map<String, Object> paths = scanJmxControllers();
		openApi.put("paths", paths);

		// 组件定义
		final Map<String, Object> components = new LinkedHashMap<>();
		final Map<String, Object> schemas = new LinkedHashMap<>();
		components.put("schemas", schemas);
		openApi.put("components", components);

		return openApi;
	}

	@GetMapping(value = SWAGGER_CONFIG_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getSwaggerConfig() {
		final Map<String, Object> config = new LinkedHashMap<>();

		config.put("configUrl", SWAGGER_CONFIG_PATH);
		config.put("oauth2RedirectUrl",
				"http://127.0.0.1:" + properties.getPort() + "/swagger-ui/oauth2-redirect.html");
		config.put("url", OPENAPI_PATH);
		config.put("validatorUrl", "");

		return config;
	}

	/**
	 * 扫描JMX Console的Controller，构建路径信息
	 */
	private Map<String, Object> scanJmxControllers() {
		final Map<String, Object> paths = new LinkedHashMap<>();
		logger.debug("扫描JMX Console的Controller，构建路径信息: {}", CONTROLLER_PACKAGE);

		try {
			final RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
			final Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

			for (final Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
				final RequestMappingInfo mappingInfo = entry.getKey();
				final HandlerMethod handlerMethod = entry.getValue();

				// 只处理JMX Console包下的Controller
				final String controllerPackage = handlerMethod.getBeanType().getPackage().getName();
				if (!controllerPackage.startsWith(CONTROLLER_PACKAGE)) {
					logger.debug("跳过非JMX Console包的Controller: {}", handlerMethod.getMethod().getName());
					continue;
				}

				// 跳过自己这个Controller的OpenAPI端点，但保留swagger-config端点
				if (handlerMethod.getBeanType() == OpenApiController.class) {
					final String methodName = handlerMethod.getMethod().getName();
					if (!"getSwaggerConfig".equals(methodName)) {
						continue;
					}
				}

				logger.debug("扫描到Controller: {}#{}", handlerMethod.getBeanType().getName(),
						handlerMethod.getMethod().getName());
				// 构建路径信息
				if (mappingInfo.getPathPatternsCondition() != null) {
					mappingInfo.getPathPatternsCondition().getPatterns().forEach(pattern -> {
						final String path = pattern.getPatternString();
						@SuppressWarnings("unchecked")
						final Map<String, Object> pathItem = (Map<String, Object>) paths.computeIfAbsent(path,
								k -> new LinkedHashMap<>());

						// 获取HTTP方法
						mappingInfo.getMethodsCondition().getMethods().forEach(httpMethod -> {
							final Map<String, Object> operation = buildOperation(handlerMethod);
							pathItem.put(httpMethod.name().toLowerCase(), operation);
						});
					});
				}
			}
		} catch (final Exception e) {
			// 如果扫描失败，至少返回基本信息
			final Map<String, Object> healthPath = new LinkedHashMap<>();
			final Map<String, Object> getOperation = new LinkedHashMap<>();
			getOperation.put("summary", "健康检查");
			getOperation.put("operationId", "health");

			final Map<String, Object> responses = new LinkedHashMap<>();
			final Map<String, Object> response200 = new LinkedHashMap<>();
			response200.put("description", "成功");
			responses.put("200", response200);
			getOperation.put("responses", responses);

			healthPath.put("get", getOperation);
			paths.put("/health", healthPath);
		}

		return paths;
	}

	/**
	 * 构建操作信息
	 */
	private Map<String, Object> buildOperation(HandlerMethod handlerMethod) {
		final Map<String, Object> operation = new LinkedHashMap<>();

		final Method method = handlerMethod.getMethod();

		// 从注解获取信息
		final Operation opAnnotation = method.getAnnotation(Operation.class);
		if (opAnnotation != null) {
			operation.put("summary", opAnnotation.summary());
			operation.put("description", opAnnotation.description());
		} else {
			operation.put("summary", method.getName());
		}

		// 设置operationId
		operation.put("operationId", method.getName());

		// 获取Controller的Tag信息
		final Tag tagAnnotation = handlerMethod.getBeanType().getAnnotation(Tag.class);
		if (tagAnnotation != null) {
			operation.put("tags", new String[] { tagAnnotation.name() });
		}

		// 基本响应
		final Map<String, Object> responses = new LinkedHashMap<>();
		final Map<String, Object> response200 = new LinkedHashMap<>();
		response200.put("description", "成功");
		responses.put("200", response200);
		operation.put("responses", responses);

		return operation;
	}
}
