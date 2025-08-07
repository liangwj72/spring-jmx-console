package com.liangwj.spring.jmxConsole.controllers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liangwj.spring.jmxConsole.config.JmxConsoleProperties;
import com.liangwj.spring.jmxConsole.utils.MethodUtil;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;

/**
 * 手动实现的OpenAPI端点 - 使用io.swagger.v3包生成完整的API文档
 * 只扫描JMX Console自己的Controller，避免与主项目冲突
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
	private static final String CONTROLLER_PACKAGE = HealthController.class.getPackageName();

	// 缓存生成的OpenAPI对象
	private volatile OpenAPI cachedOpenAPI;
	private volatile Map<String, Schema<?>> cachedSchemas;

	@GetMapping(value = OPENAPI_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getOpenApiSpec() throws JsonProcessingException {
		OpenAPI openAPI = getCachedOpenAPI();
		return Json.mapper().writeValueAsString(openAPI);
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
	 * 获取缓存的OpenAPI对象，如果不存在则构建
	 */
	private OpenAPI getCachedOpenAPI() {
		if (cachedOpenAPI == null) {
			synchronized (this) {
				if (cachedOpenAPI == null) {
					// 清空Schema缓存，重新生成
					cachedSchemas = null;
					cachedOpenAPI = buildOpenAPI();
				}
			}
		}
		return cachedOpenAPI;
	}

	/**
	 * 构建完整的OpenAPI对象
	 */
	private OpenAPI buildOpenAPI() {
		logger.debug("构建JMX Console OpenAPI文档...");

		// 先预处理所有Schema，确保嵌套对象都被正确生成
		preGenerateAllSchemas();

		OpenAPI openAPI = new OpenAPI()
			.info(new Info()
				.title("JMX Console API")
				.description("JMX Console管理接口 - 独立运行端口")
				.version("1.0.0"))
			.paths(buildPaths())
			.components(buildComponents());

		Map<String, Schema<?>> schemas = getCachedSchemas();
		logger.debug("OpenAPI文档构建完成，包含 {} 个路径，{} 个Schema", 
			openAPI.getPaths() != null ? openAPI.getPaths().size() : 0,
			schemas.size());

		if (logger.isDebugEnabled()) {
			logger.debug("生成的Schema列表: {}", schemas.keySet());
		}

		return openAPI;
	}

	/**
	 * 预先生成所有需要的Schema，特别是嵌套对象的Schema
	 */
	private void preGenerateAllSchemas() {
		logger.debug("开始预生成所有Schema...");
		
		RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			HandlerMethod handlerMethod = entry.getValue();

			// 只处理JMX Console包下的Controller
			String controllerPackage = handlerMethod.getBeanType().getPackage().getName();
			if (!controllerPackage.startsWith(CONTROLLER_PACKAGE)) {
				continue;
			}

			// 跳过自己这个Controller的OpenAPI端点
			if (handlerMethod.getBeanType() == OpenApiController.class) {
				continue;
			}

			// 预生成返回类型的Schema
			Class<?> returnType = getActualReturnType(handlerMethod.getMethod());
			if (returnType != null && returnType != Void.class && returnType != void.class) {
				generateSchemaFromClass(returnType);
				logger.debug("为方法 {}#{} 预生成返回类型Schema: {}", 
					handlerMethod.getBeanType().getSimpleName(),
					handlerMethod.getMethod().getName(),
					returnType.getSimpleName());
			}
		}
		
		Map<String, Schema<?>> schemas = getCachedSchemas();
		logger.debug("预生成完成，总共生成了 {} 个Schema: {}", schemas.size(), schemas.keySet());
	}

	/**
	 * 构建路径信息
	 */
	private Paths buildPaths() {
		Paths paths = new Paths();
		RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			RequestMappingInfo mappingInfo = entry.getKey();
			HandlerMethod handlerMethod = entry.getValue();

			// 只处理JMX Console包下的Controller
			String controllerPackage = handlerMethod.getBeanType().getPackage().getName();
			if (!controllerPackage.startsWith(CONTROLLER_PACKAGE)) {
				continue;
			}

			// 跳过自己这个Controller的OpenAPI端点
			if (handlerMethod.getBeanType() == OpenApiController.class) {
				continue;
			}

			// 处理路径模式
			if (mappingInfo.getPathPatternsCondition() != null) {
				mappingInfo.getPathPatternsCondition().getPatterns().forEach(pattern -> {
					String path = pattern.getPatternString();
					PathItem pathItem = paths.get(path);
					if (pathItem == null) {
						pathItem = new PathItem();
						paths.addPathItem(path, pathItem);
					}

					// 处理HTTP方法
					Set<org.springframework.web.bind.annotation.RequestMethod> methods = 
						mappingInfo.getMethodsCondition().getMethods();
					
					if (methods.isEmpty()) {
						// 如果没有指定方法，默认为GET
						pathItem.setGet(buildOperation(handlerMethod));
					} else {
						for (org.springframework.web.bind.annotation.RequestMethod method : methods) {
							io.swagger.v3.oas.models.Operation operation = buildOperation(handlerMethod);
							switch (method) {
								case GET:
									pathItem.setGet(operation);
									break;
								case POST:
									pathItem.setPost(operation);
									break;
								case PUT:
									pathItem.setPut(operation);
									break;
								case DELETE:
									pathItem.setDelete(operation);
									break;
								case PATCH:
									pathItem.setPatch(operation);
									break;
								case HEAD:
									pathItem.setHead(operation);
									break;
								case OPTIONS:
									pathItem.setOptions(operation);
									break;
								case TRACE:
									pathItem.setTrace(operation);
									break;
							}
						}
					}
				});
			}
		}

		return paths;
	}

	/**
	 * 构建操作信息
	 */
	private io.swagger.v3.oas.models.Operation buildOperation(HandlerMethod handlerMethod) {
		io.swagger.v3.oas.models.Operation operation = new io.swagger.v3.oas.models.Operation();
		Method method = handlerMethod.getMethod();

		// 解析@Operation注解
		Operation opAnnotation = method.getAnnotation(Operation.class);
		if (opAnnotation != null) {
			operation.summary(opAnnotation.summary());
			operation.description(opAnnotation.description());
			operation.operationId(opAnnotation.operationId().isEmpty() ? method.getName() : opAnnotation.operationId());
		} else {
			operation.summary(method.getName());
			operation.operationId(method.getName());
		}

		// 获取Controller的Tag信息
		Tag tagAnnotation = handlerMethod.getBeanType().getAnnotation(Tag.class);
		if (tagAnnotation != null) {
			operation.addTagsItem(tagAnnotation.name());
		}

		// 构建响应信息
		operation.responses(buildResponses(handlerMethod));

		return operation;
	}

	/**
	 * 构建响应信息
	 */
	private io.swagger.v3.oas.models.responses.ApiResponses buildResponses(HandlerMethod handlerMethod) {
		io.swagger.v3.oas.models.responses.ApiResponses responses = new io.swagger.v3.oas.models.responses.ApiResponses();
		Method method = handlerMethod.getMethod();

		// 解析@ApiResponses注解
		ApiResponses apiResponsesAnnotation = method.getAnnotation(ApiResponses.class);
		if (apiResponsesAnnotation != null) {
			for (ApiResponse apiResponse : apiResponsesAnnotation.value()) {
				io.swagger.v3.oas.models.responses.ApiResponse response = 
					new io.swagger.v3.oas.models.responses.ApiResponse();
				response.description(apiResponse.description());
				
				// 如果有content定义，添加媒体类型
				if (apiResponse.content().length > 0) {
					Content content = new Content();
					for (io.swagger.v3.oas.annotations.media.Content contentAnnotation : apiResponse.content()) {
						io.swagger.v3.oas.models.media.MediaType mediaTypeObj = new io.swagger.v3.oas.models.media.MediaType();
						
						// 生成Schema
						if (contentAnnotation.schema().implementation() != Void.class) {
							Schema<?> schema = generateSchemaFromClass(contentAnnotation.schema().implementation());
							mediaTypeObj.schema(schema);
						}
						
						// 使用正确的媒体类型，如果为空则默认为application/json
						String mediaType = contentAnnotation.mediaType();
						if (mediaType == null || mediaType.trim().isEmpty()) {
							mediaType = "application/json";
						}
						content.addMediaType(mediaType, mediaTypeObj);
					}
					response.content(content);
				}
				
				responses.addApiResponse(apiResponse.responseCode(), response);
			}
		}

		// 如果没有@ApiResponses注解，根据返回类型生成默认响应
		if (responses.isEmpty()) {
			io.swagger.v3.oas.models.responses.ApiResponse defaultResponse = 
				new io.swagger.v3.oas.models.responses.ApiResponse();
			defaultResponse.description("成功");

			// 生成返回类型的Schema
			Class<?> returnType = getActualReturnType(method);
			if (returnType != null && returnType != Void.class && returnType != void.class) {
				Content content = new Content();
				io.swagger.v3.oas.models.media.MediaType mediaTypeObj = new io.swagger.v3.oas.models.media.MediaType();
				Schema<?> schema = generateSchemaFromClass(returnType);
				mediaTypeObj.schema(schema);
				content.addMediaType("application/json", mediaTypeObj);
				defaultResponse.content(content);
			}

			responses.addApiResponse("200", defaultResponse);
		}

		return responses;
	}

	/**
	 * 获取方法的实际返回类型
	 */
	private Class<?> getActualReturnType(Method method) {
		Type returnType = method.getGenericReturnType();
		
		// 处理ResponseEntity<T>
		if (returnType instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) returnType;
			Type rawType = paramType.getRawType();
			
			if (rawType == ResponseEntity.class) {
				Type[] actualTypes = paramType.getActualTypeArguments();
				if (actualTypes.length > 0) {
					Type actualType = actualTypes[0];
					if (actualType instanceof Class) {
						return (Class<?>) actualType;
					}
				}
			}
		}
		
		return method.getReturnType();
	}

	/**
	 * 使用ModelConverters生成Schema，确保生成所有依赖的嵌套Schema
	 */
	private Schema<?> generateSchemaFromClass(Class<?> clazz) {
		if (clazz == null || clazz == Void.class || clazz == void.class) {
			return null;
		}

		// 处理基本类型
		if (isSimpleType(clazz)) {
			return createSimpleSchema(clazz);
		}

		// 获取缓存的Schema
		Map<String, Schema<?>> schemas = getCachedSchemas();
		String schemaName = clazz.getSimpleName();
		
		// 如果已经存在，返回引用
		if (schemas.containsKey(schemaName)) {
			Schema<?> refSchema = new Schema<>();
			refSchema.$ref("#/components/schemas/" + schemaName);
			return refSchema;
		}

		// 使用递归方法生成Schema和所有依赖
		generateSchemaRecursively(clazz, schemas);
		
		// 返回对主Schema的引用
		if (schemas.containsKey(schemaName)) {
			Schema<?> refSchema = new Schema<>();
			refSchema.$ref("#/components/schemas/" + schemaName);
			return refSchema;
		}

		logger.warn("无法为类 {} 生成Schema，使用简单Schema", clazz.getName());
		// 如果生成失败，返回简单的Schema
		Schema<?> simpleSchema = new Schema<>();
		simpleSchema.type("object");
		return simpleSchema;
	}

	/**
	 * 递归生成Schema，确保所有依赖的类都被处理
	 */
	private void generateSchemaRecursively(Class<?> clazz, Map<String, Schema<?>> schemas) {
		if (clazz == null || isSimpleType(clazz) || clazz.isPrimitive()) {
			return;
		}

		String schemaName = clazz.getSimpleName();
		
		// 如果已经处理过，跳过
		if (schemas.containsKey(schemaName)) {
			return;
		}

		logger.debug("递归生成Schema: {}", schemaName);

		// 生成当前类的Schema
		ModelConverters converters = ModelConverters.getInstance();
		Map<String, Schema> resolvedSchemas = converters.read(clazz);
		
		if (resolvedSchemas != null && !resolvedSchemas.isEmpty()) {
			// 缓存所有生成的Schema
			for (Map.Entry<String, Schema> entry : resolvedSchemas.entrySet()) {
				if (!schemas.containsKey(entry.getKey())) {
					schemas.put(entry.getKey(), entry.getValue());
					logger.debug("缓存Schema: {}", entry.getKey());
				}
			}

			// 通过反射分析嵌套类型，确保它们也被生成
			processNestedSchemasByReflection(clazz, schemas);
		}
	}

	/**
	 * 通过反射分析类的getter方法和字段，自动发现并生成嵌套Schema
	 */
	private void processNestedSchemasByReflection(Class<?> clazz, Map<String, Schema<?>> schemas) {
		logger.debug("通过反射分析类 {} 的嵌套类型", clazz.getSimpleName());
		
		try {
			// 先尝试通过getter方法分析
			List<MethodUtil.MethodInfoOfGetter> getters = MethodUtil.findGetter(clazz);
			
			for (MethodUtil.MethodInfoOfGetter getter : getters) {
				Class<?> returnType = getter.getReturnTypeClass();
				processNestedType(returnType, getter.getPropName(), schemas);
			}
			
			// 如果没有找到getter方法，或者getter数量很少，也通过字段分析
			if (getters.size() < 3) { // 如果getter很少，可能这个类主要使用字段而不是getter
				analyzeFieldTypes(clazz, schemas);
			}
			
		} catch (Exception e) {
			logger.warn("分析类 {} 的嵌套类型时出错: {}", clazz.getSimpleName(), e.getMessage());
		}
	}

	/**
	 * 分析类的字段类型
	 */
	private void analyzeFieldTypes(Class<?> clazz, Map<String, Schema<?>> schemas) {
		logger.debug("分析类 {} 的字段类型", clazz.getSimpleName());
		
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			// 跳过静态字段、常量等
			if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
				java.lang.reflect.Modifier.isFinal(field.getModifiers()) ||
				field.getName().equals("serialVersionUID")) {
				continue;
			}
			
			Class<?> fieldType = field.getType();
			
			// 处理集合类型
			if (List.class.isAssignableFrom(fieldType) || fieldType.isArray()) {
				fieldType = getCollectionElementType(field);
			}
			
			processNestedType(fieldType, field.getName(), schemas);
		}
	}

	/**
	 * 获取集合或数组的元素类型
	 */
	private Class<?> getCollectionElementType(Field field) {
		Type genericType = field.getGenericType();
		
		if (genericType instanceof ParameterizedType) {
			ParameterizedType paramType = (ParameterizedType) genericType;
			Type[] actualTypes = paramType.getActualTypeArguments();
			if (actualTypes.length > 0 && actualTypes[0] instanceof Class) {
				return (Class<?>) actualTypes[0];
			}
		} else if (field.getType().isArray()) {
			return field.getType().getComponentType();
		}
		
		return field.getType();
	}

	/**
	 * 处理发现的嵌套类型
	 */
	private void processNestedType(Class<?> type, String propertyName, Map<String, Schema<?>> schemas) {
		// 跳过简单类型和已经处理过的类型
		if (isSimpleType(type) || type.isPrimitive()) {
			return;
		}
		
		// 跳过已经处理过的Schema
		if (schemas.containsKey(type.getSimpleName())) {
			return;
		}
		
		logger.debug("发现嵌套类型: {} -> {}", propertyName, type.getSimpleName());
		
		// 递归生成嵌套类型的Schema
		generateSchemaRecursively(type, schemas);
	}

	/**
	 * 检查是否为简单类型
	 */
	private boolean isSimpleType(Class<?> clazz) {
		return clazz.isPrimitive() ||
			String.class.equals(clazz) ||
			Number.class.isAssignableFrom(clazz) ||
			Boolean.class.equals(clazz) ||
			java.util.Date.class.equals(clazz) ||
			java.time.LocalDate.class.equals(clazz) ||
			java.time.LocalDateTime.class.equals(clazz) ||
			java.time.LocalTime.class.equals(clazz);
	}

	/**
	 * 为简单类型创建Schema
	 */
	private Schema<?> createSimpleSchema(Class<?> clazz) {
		Schema<?> schema = new Schema<>();
		
		if (clazz.isPrimitive() || Number.class.isAssignableFrom(clazz)) {
			if (clazz == int.class || clazz == Integer.class || 
				clazz == long.class || clazz == Long.class) {
				schema.type("integer");
			} else if (clazz == double.class || clazz == Double.class ||
					   clazz == float.class || clazz == Float.class) {
				schema.type("number");
			} else {
				schema.type("integer");
			}
		} else if (clazz == boolean.class || clazz == Boolean.class) {
			schema.type("boolean");
		} else {
			schema.type("string");
		}
		
		return schema;
	}

	/**
	 * 获取缓存的Schema集合
	 */
	private Map<String, Schema<?>> getCachedSchemas() {
		if (cachedSchemas == null) {
			synchronized (this) {
				if (cachedSchemas == null) {
					cachedSchemas = new LinkedHashMap<>();
				}
			}
		}
		return cachedSchemas;
	}

	/**
	 * 构建组件信息
	 */
	private Components buildComponents() {
		Components components = new Components();
		
		// 添加Schema定义
		Map<String, Schema<?>> schemas = getCachedSchemas();
		if (!schemas.isEmpty()) {
			// 转换类型以满足Components.schemas()的要求
			Map<String, Schema> rawSchemas = new LinkedHashMap<>();
			for (Map.Entry<String, Schema<?>> entry : schemas.entrySet()) {
				rawSchemas.put(entry.getKey(), entry.getValue());
			}
			components.schemas(rawSchemas);
		}

		return components;
	}
}
