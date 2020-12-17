package ch.l0r5.autotrader.authentication;

import com.google.common.hash.Hashing;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import ch.l0r5.autotrader.Operation;
import lombok.extern.slf4j.Slf4j;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@SuppressWarnings("UnstableApiUsage")
public class ApiKeySignatureBuilder {

    public String createSignature(Map<String, String> qParams, Operation operation) {
        byte[] byteKey = readSecret();
        long nonce = new Date().getTime();
        String message = "nonce=" + nonce + getQueryString(qParams);
        String urlPath = "/0/private/" + operation.getCode();
        byte[] sha256 = Hashing.sha256()
                .newHasher()
                .putString(nonce + message, UTF_8)
                .hash()
                .asBytes();
        byte[] hmac = Hashing.hmacSha512(byteKey)
                .newHasher()
                .putString(urlPath, UTF_8)
                .putBytes(sha256)
                .hash()
                .asBytes();
        String signature = Base64.getEncoder().encodeToString(hmac);
        log.info("Signature sucessfully created.");
        return signature;
    }

    private String getQueryString(Map<String, String> qParams) {
        StringBuilder result = new StringBuilder();
        qParams.forEach((k, v) -> {
            result.append("&").append(k).append("=").append(v);
        });
        return result.toString();
    }

    protected byte[] readSecret() {
        File secretFile = new File("src/main/resources/secrets/private-key-api.txt");
        String secretData = null;
        try {
            Scanner scanner = new Scanner(secretFile);
            while (scanner.hasNextLine()) {
                secretData = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            log.error("API Secret not found", e);
        }
        if (secretData == null) log.error("API Secret File has no content.");
        return Base64.getDecoder().decode(secretData);
    }
}
