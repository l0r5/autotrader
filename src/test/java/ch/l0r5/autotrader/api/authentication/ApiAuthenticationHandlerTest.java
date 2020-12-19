package ch.l0r5.autotrader.api.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import ch.l0r5.autotrader.api.controllers.Operation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApiAuthenticationHandlerTest {

    private ApiAuthenticationHandler builder;

    @BeforeEach
    void setUp() {
        builder = new ApiAuthenticationHandler();
    }

    @Test
    void testCreateSignature_withBalance_expectSignature() {
        Map<String, String> qParams = new HashMap<>();
        qParams.put("asset", "xxbt");
        String path = "/0/private/" + Operation.BALANCE.getCode();
        ApiKeySignature signature = builder.createSignature(qParams, path);
        assertFalse(signature.getSignature().isEmpty());
    }

    @Test
    void testCreateSignature_withTradeBalance_expectSignature() {
        Map<String, String> qParams = new HashMap<>();
        qParams.put("asset", "zusd");
        String path = "/0/private/" + Operation.TRADEBALANCE.getCode();
        ApiKeySignature signature = builder.createSignature(qParams, path);
        assertFalse(signature.getSignature().isEmpty());
    }

    @Test
    void testCreateSignature_withOpenOrders_expectSignature() {
        Map<String, String> qParams = new HashMap<>();
        String path = "/0/private/" + Operation.OPENORDERS.getCode();
        ApiKeySignature signature = builder.createSignature(qParams, path);
        assertFalse(signature.getSignature().isEmpty());
    }

    @Test
    void testGetApiPrivateKey_expectSecret() {
        assertNotNull(builder.getAPI_PRIVATE_KEY());
    }

    @Test
    void testGetApiPublicKey_expectPublicKey() {
        assertNotNull(builder.getAPI_PUBLIC_KEY());
    }

}