package ch.l0r5.autotrader.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class ApiKeySignatureBuilderTest {

    private ApiKeySignatureBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new ApiKeySignatureBuilder();
    }

    @Test
    void testCreateSignature_expectSignature() {
        assertFalse(builder.createSignature().isEmpty());
    }


}