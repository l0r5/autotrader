package ch.l0r5.autotrader.api.controllers;

public enum Operation {
    BALANCE("Balance"),
    OPENORDERS("OpenOrders"),
    CANCELORDER("CancelOrder"),
    TICKER("Ticker");

    private final String code;

    Operation(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
