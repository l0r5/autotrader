package ch.l0r5.autotrader.api.controllers;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestHandler {

    private final ApiAuthenticationHandler authHandler;
    private final ApiConfig apiConfig;

    public RestHandler(ApiAuthenticationHandler authHandler, ApiConfig apiConfig) {
        this.authHandler = authHandler;
        this.apiConfig = apiConfig;
    }

    protected String makePostCall(Map<String, String> qParams, String path) {
        ApiKeySignature signature = authHandler.createSignature(qParams, path);
        HttpHeaders headers = createHttpHeaders(signature);
        MultiValueMap<String, String> content = createMessageBody(qParams, signature);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(content, headers);
        log.info("Executing POST call with path: {}, qparams: {}", path, qParams.toString());
        ResponseEntity<String> response = new RestTemplate().postForEntity(apiConfig.getBaseUrl() + path, request, String.class);
        log.info("POST Response: {}", response);
        return response.getBody();
    }

    private HttpHeaders createHttpHeaders(ApiKeySignature signature) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("API-Key", authHandler.getApiPublicKey());
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
