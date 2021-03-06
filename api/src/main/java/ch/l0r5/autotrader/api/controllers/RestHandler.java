package ch.l0r5.autotrader.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import ch.l0r5.autotrader.api.authentication.ApiAuthenticationHandler;
import ch.l0r5.autotrader.api.authentication.ApiKeySignature;
import ch.l0r5.autotrader.api.config.ApiConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Getter
@AllArgsConstructor
public class RestHandler {

    private final RestTemplate restTemplate;
    private final ApiAuthenticationHandler authHandler;
    private final ApiConfig apiConfig;

    @Autowired
    public RestHandler(ApiAuthenticationHandler authHandler, ApiConfig apiConfig) {
        this.authHandler = authHandler;
        this.apiConfig = apiConfig;
        this.restTemplate = new RestTemplate();
    }

    protected String makePostCall(Map<String, String> qParams, String path) {
        ApiKeySignature signature = authHandler.createSignature(qParams, path);
        HttpHeaders headers = createHttpHeaders(signature);
        MultiValueMap<String, String> content = createMessageBody(qParams, signature);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(content, headers);
        log.info("Executing POST call with path: {}, qparams: {}", path, qParams.toString());
        ResponseEntity<String> response = restTemplate.postForEntity(apiConfig.getBaseUrl() + path, request, String.class);
        log.info("POST Response: {}", response);
        return response.getBody();
    }

    protected HttpHeaders createHttpHeaders(ApiKeySignature signature) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("API-Key", authHandler.getApiPublicKey());
        headers.set("API-Sign", signature.getSignature());
        return headers;
    }

    protected MultiValueMap<String, String> createMessageBody(Map<String, String> qParams, ApiKeySignature signature) {
        MultiValueMap<String, String> content = new LinkedMultiValueMap<>();
        content.add("nonce", String.valueOf(signature.getNonce()));
        qParams.forEach(content::add);
        return content;
    }
}
