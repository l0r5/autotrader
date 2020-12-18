package ch.l0r5.autotrader;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import ch.l0r5.autotrader.authentication.ApiAuthenticationHandler;
import ch.l0r5.autotrader.authentication.ApiKeySignature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Broker {

    private static final String BASE_URL = "https://api.kraken.com";
    private final ApiAuthenticationHandler authHandler;

    public Broker(ApiAuthenticationHandler authHandler) {
        this.authHandler = authHandler;
    }

    ResponseEntity<String> getCurrentBalance() {
        Map<String, String> qParams = Collections.singletonMap("asset", "xxbt");
        String path = "/0/private/" + Operation.BALANCE.getCode();
        ApiKeySignature signature = authHandler.createSignature(qParams, path);
        HttpHeaders headers = createHttpHeaders(signature);
        MultiValueMap<String, String> content = createMessageBody(qParams, signature);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(content, headers);
        return new RestTemplate().postForEntity(BASE_URL + path, request, String.class);
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
