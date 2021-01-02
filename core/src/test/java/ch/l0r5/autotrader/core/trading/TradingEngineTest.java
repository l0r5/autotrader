package ch.l0r5.autotrader.core.trading;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.l0r5.autotrader.model.Trade;
import ch.l0r5.autotrader.model.enums.OrderType;
import ch.l0r5.autotrader.model.enums.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TradingEngineTest {

    TradingEngine tradingEngine;

    @BeforeEach
    void init() {
        tradingEngine = new TradingEngine();
    }

    @Test
    void testCalcVWAPs_inputTrades_expectVWAPs() {
        List<Trade> testTrades = new ArrayList<>();
        testTrades.add(Trade.builder()
                .price(new BigDecimal("300"))
                .volume(new BigDecimal("5000"))
                .time(1609264800L)
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .miscellaneous("test")
                .build());
        testTrades.add(Trade.builder()
                .price(new BigDecimal("320"))
                .volume(new BigDecimal("200"))
                .time(1609264800L)
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .miscellaneous("test")
                .build());
        testTrades.add(Trade.builder()
                .price(new BigDecimal("300"))
                .volume(new BigDecimal("3000"))
                .time(1609264810L)
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .miscellaneous("test")
                .build());
        Map<Long, Map<String, BigDecimal>> resultVWMA = tradingEngine.calcVWMA(testTrades, 10);
        assertEquals(2, resultVWMA.size());
        assertEquals(new BigDecimal("300.76923077"), resultVWMA.get(1609264800L).get("vwap"));
        assertEquals(new BigDecimal("5200"), resultVWMA.get(1609264800L).get("volume"));
        assertEquals(new BigDecimal("300.00000000"), resultVWMA.get(1609264810L).get("vwap"));
        assertEquals(new BigDecimal("3000"), resultVWMA.get(1609264810L).get("volume"));
    }

    @Test
    void testCalcIntervalVWAP_expectVWAP() {
        List<BigDecimal> pricesPerInterval = new ArrayList<>();
        List<BigDecimal> volumesPerInterval = new ArrayList<>();
        pricesPerInterval.add(new BigDecimal("100"));
        pricesPerInterval.add(new BigDecimal("200"));
        volumesPerInterval.add(new BigDecimal("4"));
        volumesPerInterval.add(new BigDecimal("2"));
        assertEquals(new BigDecimal("133.33333334"), tradingEngine.calcIntervalVWAP(pricesPerInterval, volumesPerInterval));
    }

    @Test
    void testCalcIntervalVolumeSum_expectVolumeSum() {
        List<BigDecimal> volumesPerInterval = new ArrayList<>();
        volumesPerInterval.add(new BigDecimal("4"));
        volumesPerInterval.add(new BigDecimal("2"));
        assertEquals(new BigDecimal("6"), tradingEngine.calcIntervalVolumeSum(volumesPerInterval));
    }

}