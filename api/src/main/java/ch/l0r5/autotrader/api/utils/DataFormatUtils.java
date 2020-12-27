package ch.l0r5.autotrader.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class DataFormatUtils {

    public static String getQueryString(Map<String, String> qParams) {
        StringBuilder result = new StringBuilder();
        qParams.forEach((k, v) -> result.append("&").append(k).append("=").append(v));
        return result.toString();
    }

    public static class Json {

        private static ObjectMapper objectMapper = getDefaultObjectMapper();

        private static ObjectMapper getDefaultObjectMapper() {
            return new ObjectMapper();
        }

        public static JsonNode parse(String src) throws JsonProcessingException {
            return objectMapper.readTree(src);
        }

        public static <T> T fromJson(JsonNode node, Class<T> clazz) throws JsonProcessingException {
            return objectMapper.treeToValue(node, clazz);
        }

    }
}
