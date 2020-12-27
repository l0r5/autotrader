package ch.l0r5.autotrader.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.jupiter.api.Test;

import ch.l0r5.autotrader.api.utils.pojo.TestJsonPojo;

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
        TestJsonPojo result = null;
        try {
            JsonNode node = DataFormatUtils.Json.parse(testString);
            result = DataFormatUtils.Json.fromJson(node, TestJsonPojo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertNotNull(result);
        assertNotNull(result.getTitle());
        assertEquals("This is my Test", result.getTitle());
    }
}