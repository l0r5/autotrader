package ch.l0r5.autotrader.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ch.l0r5.autotrader.Operation;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ApiKeySignatureBuilderTest {

    private ApiKeySignatureBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new ApiKeySignatureBuilder();
    }

    @Test
    void testCreateSignature_withBalance_expectSignature() {
        Map<String, String> qParams = new HashMap<>();
        qParams.put("asset", "xxbt");
        assertFalse(builder.createSignature(qParams, Operation.BALANCE).isEmpty());
    }

    @Test
    void testReadSecret_expectSecret() {
        assertFalse(builder.readSecret() == null);
    }

}