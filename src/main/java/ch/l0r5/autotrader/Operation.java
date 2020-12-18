package ch.l0r5.autotrader;

public enum Operation {
    BALANCE("Balance"),
    TRADEBALANCE("TradeBalance");

    private final String code;

    Operation(String code) {
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
