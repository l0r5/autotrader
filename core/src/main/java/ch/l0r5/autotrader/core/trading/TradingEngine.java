package ch.l0r5.autotrader.core.trading;

import java.math.BigDecimal;

import ch.l0r5.autotrader.model.enums.Type;

public class TradingEngine {

    public BigDecimal getSignal(Type tradingMode) {
        if (tradingMode == Type.BUY) return calcBuySignal();
        else return calcSellSignal();
    }

    private BigDecimal calcBuySignal() {
        return new BigDecimal("0");
    }

    private BigDecimal calcSellSignal() {
        return new BigDecimal("0");
    }
}
