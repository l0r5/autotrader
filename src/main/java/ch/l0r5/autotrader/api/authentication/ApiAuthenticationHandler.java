package ch.l0r5.autotrader.api.authentication;

import com.google.common.hash.Hashing;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static ch.l0r5.autotrader.utils.DataFormatUtils.getQueryString;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Getter
@Component
@SuppressWarnings("UnstableApiUsage")
public class ApiAuthenticationHandler {

    private final String API_PUBLIC_KEY = readFromFile(KeyType.PUBLIC);
    private final byte[] API_PRIVATE_KEY = Base64.getDecoder().decode(readFromFile(KeyType.PRIVATE));

    public ApiKeySignature createSignature(Map<String, String> qParams, String urlPath) {
        long nonce = new Date().getTime();
        String message = "nonce=" + nonce + getQueryString(qParams);
        byte[] sha256 = Hashing.sha256()
                .newHasher()
                .putString(nonce + message, UTF_8)
                .hash()
                .asBytes();
        byte[] hmac = Hashing.hmacSha512(API_PRIVATE_KEY)
                .newHasher()
                .putString(urlPath, UTF_8)
                .putBytes(sha256)
                .hash()
                .asBytes();
        String signature = Base64.getEncoder().encodeToString(hmac);
        log.info("Signature successfully created.");
        return ApiKeySignature.builder()
                .nonce(nonce)
                .urlPath(urlPath)
                .message(message)
                .signature(signature)
                .build();
    }

    private String readFromFile(KeyType keyType) {
        File secretFile = new File("src/main/resources/secrets/" + keyType.getCode() + ".txt");
        String keyData = null;
        try {
            Scanner scanner = new Scanner(secretFile);
            while (scanner.hasNextLine()) {
                keyData = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            log.error("API Secret not found", e);
        }
        if (keyData == null) log.error("API Secret File has no content.");
        return keyData;
    }
}