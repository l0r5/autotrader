package ch.l0r5.autotrader.api.authentication;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiKeySignature {
    private final long nonce;
    private final String message;
    private final String urlPath;
    private final String signature;
}
