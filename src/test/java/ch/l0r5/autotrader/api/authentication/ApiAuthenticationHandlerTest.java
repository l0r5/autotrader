package ch.l0r5.autotrader.api.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
        assertFalse(builder.createSignature(qParams, "Operation.BALANCE").getSignature().isEmpty());
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