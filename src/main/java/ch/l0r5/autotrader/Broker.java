package ch.l0r5.autotrader;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import ch.l0r5.autotrader.authentication.ApiKeySignatureBuilder;

@Service
public class Broker {

    private final ApiKeySignatureBuilder signatureBuilder;

    public Broker(ApiKeySignatureBuilder signatureBuilder) {
        this.signatureBuilder = signatureBuilder;
        getCurrentBalance();
    }

    long getCurrentBalance() {
        Map<String, String> qParams = new HashMap<>();
        qParams.put("asset", "xxbt");
        String signature = signatureBuilder.createSignature(qParams, Operation.BALANCE);
        return 0;
    }


}
