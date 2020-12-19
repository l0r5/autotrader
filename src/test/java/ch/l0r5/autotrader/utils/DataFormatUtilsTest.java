package ch.l0r5.autotrader.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Test;

import ch.l0r5.autotrader.utils.pojo.TestJson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataFormatUtilsTest {

    @Test
    void testJsonParse_expectJson() {
        String testString = "{ \"title\": \"This is my Test\" }";
        JsonNode result = null;
        try {
            result = DataFormatUtils.Json.parse(testString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertNotNull(result);
        assertNotNull(result.get("title"));
        assertEquals("This is my Test", result.get("title").asText());
    }

    @Test
    void testJsonFromJson_expectTestJsonPojo() {
        String testString = "{ \"title\": \"This is my Test\" }";
        TestJson result = null;
        try {
            JsonNode node = DataFormatUtils.Json.parse(testString);
            result = DataFormatUtils.Json.fromJson(node, TestJson.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertNotNull(result);
        assertNotNull(result.getTitle());
        assertEquals("This is my Test", result.getTitle());
    }
}