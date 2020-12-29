package ch.l0r5.autotrader.api.authentication;

import com.google.common.hash.Hashing;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import ch.l0r5.autotrader.api.config.ApiConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Getter
@Component
@SuppressWarnings("UnstableApiUsage")
public class ApiAuthenticationHandler {

    private final ApiConfig apiConfig;
    private String apiPublicKey;
    private byte[] apiPrivateKey;

    public ApiAuthenticationHandler(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    @PostConstruct
    protected void init() {
        this.apiPublicKey = readFromFile(apiConfig.getPublicKeyLocation());
        this.apiPrivateKey = Base64.getDecoder().decode(readFromFile(apiConfig.getPrivateKeyLocation()));
    }

    public ApiKeySignature createSignature(Map<String, String> qParams, String urlPath) {
        long nonce = new Date().getTime();
        String message = "nonce=" + nonce + getQueryString(qParams);
        byte[] sha256 = Hashing.sha256()
                .newHasher()
                .putString(nonce + message, UTF_8)
                .hash()
                .asBytes();
        byte[] hmac = Hashing.hmacSha512(apiPrivateKey)
                .newHasher()
                .putString(urlPath, UTF_8)
                .putBytes(sha256)
                .hash()
                .asBytes();
        String signature = Base64.getEncoder().encodeToString(hmac);
        return ApiKeySignature.builder()
                .nonce(nonce)
                .urlPath(urlPath)
                .message(message)
                .signature(signature)
                .build();
    }

    protected String readFromFile(String path) {
        File secretFile = new File(path);
        String keyData = null;
        try {
            Scanner scanner = new Scanner(secretFile);
            while (scanner.hasNextLine()) {
                keyData = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            log.error("API Secret not found: ", e);
        }
        return keyData;
    }

    private String getQueryString(Map<String, String> qParams) {
        StringBuilder result = new StringBuilder();
        qParams.forEach((k, v) -> result.append("&").append(k).append("=").append(v));
        return result.toString();
    }
}
