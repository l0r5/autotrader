package ch.l0r5.autotrader.broker.enums;

public enum OrderType {
    MARKET("market"),
    LIMIT("limit"),
    STOP_LOSS("stop-loss"),
    TAKE_PROFIT("take-profit"),
    STOP_LOSS_LIMIT("stop-loss-limit"),
    TAKE_PROFIT_LIMIT("take-profit-limit"),
    SETTLE_POSITION("settle-position");

    private final String code;

    OrderType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static OrderType valueOfCode(String code) {
        for (OrderType elem : values()) {
            if (elem.code.equals(code)) return elem;
        }
        return null;
    }
}
