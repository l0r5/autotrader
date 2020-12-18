package ch.l0r5.autotrader.authentication;

public enum KeyType {
    PRIVATE("private-key-api"),
    PUBLIC("public-key-api");

    private final String code;

    KeyType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}