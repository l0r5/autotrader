package ch.l0r5.autotrader.api.enums;

public enum Operation {
    BALANCE("Balance"),
    OPEN_ORDERS("OpenOrders"),
    ADD_ORDER("AddOrder"),
    CANCEL_ORDER("CancelOrder"),
    TICKER("Ticker");

    private final String code;

    Operation(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
