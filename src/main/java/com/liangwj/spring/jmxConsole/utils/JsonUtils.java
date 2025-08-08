package com.liangwj.spring.jmxConsole.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
			logger.error(String.format("将对象转换为JSON字符串时发送错误,obj=%s", obj), e);
			return null;
        }
    }

    public static <T> T fromJsonString(String jsonString, Class<T> valueType) {
        try {
            return mapper.readValue(jsonString, valueType);
        } catch (JsonProcessingException e) {
			logger.error(String.format("从字符串解析 valueType=%s 时发送错误", valueType), e);
			return null;
        }
    }

}
