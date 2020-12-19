package ch.l0r5.autotrader.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.l0r5.autotrader.api.authentication.ApiAuthenticationHandler;
import ch.l0r5.autotrader.api.authentication.ApiKeySignature;
import ch.l0r5.autotrader.api.dto.Balance;
import ch.l0r5.autotrader.api.dto.OpenOrders;
import ch.l0r5.autotrader.utils.DataFormatUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlatformController {

    private static final String BASE_URL = "https://api.kraken.com";
    private final ApiAuthenticationHandler authHandler;

    public PlatformController(ApiAuthenticationHandler authHandler) {
        this.authHandler = authHandler;
    }

    public Balance getCurrentBalance() {
        Balance balance = new Balance();
        try {
            balance = DataFormatUtils.Json.fromJson(DataFormatUtils.Json.parse(requestCurrentBalance()), Balance.class);
        } catch (JsonProcessingException e) {
            log.error("Error during Balance update processing: ", e);
        }
        return balance;
    }

    public OpenOrders getOpenOrders() {
        OpenOrders openOrders = new OpenOrders();
        try {
            openOrders = DataFormatUtils.Json.fromJson(DataFormatUtils.Json.parse(requestOpenOrders()).get("result"), OpenOrders.class);
        } catch (JsonProcessingException e) {
            log.error("Error during OpenOrders update processing: ", e);
        }
        return openOrders;
    }

    private String requestCurrentBalance() {
        Map<String, String> qParams = Collections.singletonMap("asset", "xxbt");
        String path = "/0/private/" + Operation.BALANCE.getCode();
        return makeRestCall(qParams, path);
    }

    private String requestOpenOrders() {
        Map<String, String> qParams = new HashMap<>();
        String path = "/0/private/" + Operation.OPENORDERS.getCode();
        return makeRestCall(qParams, path);
    }

    private String makeRestCall(Map<String, String> qParams, String path) {
        ApiKeySignature signature = authHandler.createSignature(qParams, path);
        HttpHeaders headers = createHttpHeaders(signature);
        MultiValueMap<String, String> content = createMessageBody(qParams, signature);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(content, headers);
        ResponseEntity<String> response = new RestTemplate().postForEntity(BASE_URL + path, request, String.class);
        return response.getBody();
    }

    private Map<String, BigDecimal> parseBalance(String responseBody) {
        Map<String, BigDecimal> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(responseBody);
            result = mapper.convertValue(rootNode.get("result"), new TypeReference<Map<String, BigDecimal>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Map<String, String> parseOpenOrders(String responseBody) {
        Map<String, String> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(responseBody);
            result = mapper.convertValue(rootNode.get("result"), new TypeReference<Map<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    private HttpHeaders createHttpHeaders(ApiKeySignature signature) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("API-Key", authHandler.getAPI_PUBLIC_KEY());
        headers.set("API-Sign", signature.getSignature());
        return headers;
    }

    private MultiValueMap<String, String> createMessageBody(Map<String, String> qParams, ApiKeySignature signature) {
        MultiValueMap<String, String> content = new LinkedMultiValueMap<>();
        content.add("nonce", String.valueOf(signature.getNonce()));
        qParams.forEach(content::add);
        return content;
    }

}
