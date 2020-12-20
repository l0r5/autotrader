package ch.l0r5.autotrader.broker.enums;

public enum Type {
    BUY("buy"),
    SELL("sell");

    private final String code;

    Type(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
