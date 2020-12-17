package ch.l0r5.autotrader.authentication;

import com.google.common.hash.Hashing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class ApiKeySignatureBuilder {

    public String createSignature() {
        File secretFile = new File("src/main/resources/secrets/private-key-api.txt");
        String secretData = "";
        try {
            Scanner scanner = new Scanner(secretFile);
            while (scanner.hasNextLine()) {
                secretData = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            log.error("API Secret not found", e);
        }

        byte[] apiSecret = Base64.getDecoder().decode(secretData);
        long nonce = (new Date().getTime() / 1000) * 1000;
        String postData = "nonce=" + nonce + "&asset=xxbt";
        String urlPath = "/0/private/Balance";

        String sha256 = Hashing.sha256()
                .hashString(nonce + postData, UTF_8)
                .toString();

        byte[] hmac = Hashing.hmacSha512(apiSecret)
                .newHasher()
                .putString(urlPath + sha256, UTF_8)
                .hash()
                .asBytes();

        String resultSignature = Base64.getEncoder().encodeToString(hmac);

        log.info("Nonce: {}", nonce);
        log.info("Signature: {}", resultSignature);
        return resultSignature;
    }
}
