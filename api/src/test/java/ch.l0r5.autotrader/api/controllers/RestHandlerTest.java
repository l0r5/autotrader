package ch.l0r5.autotrader.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import ch.l0r5.autotrader.api.authentication.ApiAuthenticationHandler;
import ch.l0r5.autotrader.api.authentication.ApiKeySignature;
import ch.l0r5.autotrader.api.config.ApiConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RestHandlerTest {

    @Spy
    @InjectMocks
    RestHandler restHandler;

    @Mock
    RestTemplate mockRestTemplate;

    @Mock
    ApiAuthenticationHandler mockAuthHandler;

    @Mock
    ApiConfig mockApiConfig;

    private ApiKeySignature apiKeySignature;
    private Map<String, String> qParams;

    @BeforeEach
    void init() {
        this.apiKeySignature = ApiKeySignature.builder()
                .signature("0uCEneFcczGZ5LVDUlokwR3K0aqwI4y8w2pCJBVhhZE87PuMKcMMn8OXHNEsBXYhnBISLl64sgjHhc+Q3kAv9g==")
                .nonce(1609273932928L)
                .build();
        this.qParams = Collections.singletonMap("pair", "ethchf");
    }

    @Test
    void testMakePostCall_expectResponseBody() {
        String path = "0/public/Trades";
        String url = "https://test-url/" + path;
        String mockResponse = "{\"error\":[],\"result\":{\"ETHCHF\":[[\"642.28000\",\"0.02937067\",1609264847.4769,\"b\",\"l\",\"\"],[\"642.28000\",\"0.08179845\",1609264850.5637,\"b\",\"l\",\"\"],[\"642.28000\",\"0.00013067\",1609264850.5654,\"b\",\"l\",\"\"],[\"642.28000\",\"0.00000021\",1609264850.5669,\"b\",\"l\",\"\"]],\"last\":\"1609264850566852989\"}}";

        when(mockAuthHandler.createSignature(qParams, path)).thenReturn(apiKeySignature);
        when(mockApiConfig.getBaseUrl()).thenReturn("https://test-url");
        when(mockRestTemplate.postForEntity(eq("https://test-url0/public/Trades"), any(), any())).thenReturn(ResponseEntity.ok(mockResponse));
        String responseBody = restHandler.makePostCall(qParams, path);
        verify(restHandler, times(1)).createHttpHeaders(apiKeySignature);
        verify(restHandler, times(1)).createMessageBody(qParams, apiKeySignature);
        assertFalse(responseBody.isEmpty());
        assertEquals(mockResponse, responseBody);
    }

    @Test
    void testCreateHttpHeaders_expectHeaders() {
        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        expectedHeaders.set("API-Key", "TestApiKey");
        expectedHeaders.set("API-Sign", "0uCEneFcczGZ5LVDUlokwR3K0aqwI4y8w2pCJBVhhZE87PuMKcMMn8OXHNEsBXYhnBISLl64sgjHhc+Q3kAv9g==");
        when(mockAuthHandler.getApiPublicKey()).thenReturn("TestApiKey");
        HttpHeaders resultHeaders = restHandler.createHttpHeaders(apiKeySignature);
        assertNotNull(resultHeaders);
        assertTrue(new ReflectionEquals(expectedHeaders).matches(resultHeaders));
    }

    @Test
    void testCreateMessageBody_expectMessageBody() {
        String expectedResult = "{nonce=[1609273932928], pair=[ethchf]}";
        MultiValueMap<String, String> result = restHandler.createMessageBody(qParams, apiKeySignature);
        assertNotNull(result);
        assertEquals(expectedResult, result.toString());
    }
}