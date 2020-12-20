package ch.l0r5.autotrader.api.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ch.l0r5.autotrader.api.controllers.Operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
class ApiAuthenticationHandlerTest {

    @Autowired
    ApiAuthenticationHandler authHandler;

    @Test
    void testInit_keysAreNotEmpty() {
        ApiAuthenticationHandler testHandler = new ApiAuthenticationHandler(authHandler.getAppConfig());
        assertNull(testHandler.getApiPrivateKey());
        assertNull(testHandler.getApiPublicKey());
        testHandler.init();
        assertEquals("[109, 121, 45, 112, 114, 105, 118, 97, 116, 101, 45, 116, 101, 115, 116, 45, 107, 101, 121]",
                Arrays.toString(testHandler.getApiPrivateKey()));
        assertEquals("bXktcHVibGljLXRlc3Qta2V5", testHandler.getApiPublicKey());
    }

    @Test
    void testCreateSignature_withBalance_expectSignature() {
        Map<String, String> qParams = new HashMap<>();
        qParams.put("asset", "xxbt");
        String path = "/0/private/" + Operation.BALANCE.getCode();
        ApiKeySignature signature = authHandler.createSignature(qParams, path);
        assertFalse(signature.getSignature().isEmpty());
    }

    @Test
    void testCreateSignature_withOpenOrders_expectSignature() {
        Map<String, String> qParams = new HashMap<>();
        String path = "/0/private/" + Operation.OPENORDERS.getCode();
        ApiKeySignature signature = authHandler.createSignature(qParams, path);
        assertFalse(signature.getSignature().isEmpty());
    }

    @Test
    void testReadFromFile_withGoodPath_successful() {
        String path = authHandler.getAppConfig().getPrivateKeyLocation();
        String fileContent = authHandler.readFromFile(path);
        assertEquals("src/test/resources/secrets/private-key-api.txt", path);
        assertEquals("bXktcHJpdmF0ZS10ZXN0LWtleQ==", fileContent);
    }

    @Test
    void testReadFromFile_withBadPath_throwsException() {
        String path = "src/test/resources/secrets/non-existent.txt";
        authHandler.getAppConfig().setPublicKeyLocation(path);
        assertNull(authHandler.readFromFile(path));
        authHandler.getAppConfig().setPublicKeyLocation("src/test/resources/secrets/private-key-api.txt");
    }

    @Test
    void testGetApiPrivateKey_expectSecret() {
        assertNotNull(authHandler.getApiPrivateKey());
    }

    @Test
    void testGetApiPublicKey_expectPublicKey() {
        assertNotNull(authHandler.getApiPublicKey());
    }
}