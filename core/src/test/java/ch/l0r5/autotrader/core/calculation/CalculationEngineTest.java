package ch.l0r5.autotrader.core.calculation;

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


class CalculationEngineTest {

    CalculationEngine calculationEngine;

    @BeforeEach
    void init() {
        calculationEngine = new CalculationEngine();
    }

    @Test
    void testGetVWMAs_inputIntervalsForVWMA_expectVWWA() {
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
        testTrades.add(Trade.builder()
                .price(new BigDecimal("400"))
                .volume(new BigDecimal("6000"))
                .time(1609264820L)
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .miscellaneous("test")
                .build());
        testTrades.add(Trade.builder()
                .price(new BigDecimal("320"))
                .volume(new BigDecimal("4500"))
                .time(1609264900L)
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .miscellaneous("test")
                .build());
        Map<Long, Map<String, BigDecimal>> resultVWMA = calculationEngine.getVWMAs(testTrades, 10, 3);
        assertEquals(4, resultVWMA.size());
        assertEquals(new BigDecimal("300.76923077"), resultVWMA.get(1609264800L).get("vwma"));
        assertEquals(new BigDecimal("300.48780488"), resultVWMA.get(1609264810L).get("vwma"));
        assertEquals(new BigDecimal("342.53521127"), resultVWMA.get(1609264820L).get("vwma"));
        assertEquals(new BigDecimal("351.11111112"), resultVWMA.get(1609264900L).get("vwma"));
    }

    @Test
    void testCalcIntervalVMWA_inputTrades_expectVWWA() {
        List<BigDecimal> testWAPsForVWMA = new ArrayList<>();
        testWAPsForVWMA.add(new BigDecimal("300.00000000"));
        testWAPsForVWMA.add(new BigDecimal("320.00000000"));
        testWAPsForVWMA.add(new BigDecimal("400.00000000"));
        List<BigDecimal> testVolumesForVWMA = new ArrayList<>();
        testVolumesForVWMA.add(new BigDecimal("3000"));
        testVolumesForVWMA.add(new BigDecimal("4500"));
        testVolumesForVWMA.add(new BigDecimal("6000"));
        BigDecimal resultVWMA = calculationEngine.calcIntervalVWMAs(testWAPsForVWMA, testVolumesForVWMA);
        assertEquals(new BigDecimal("351.11111112"), resultVWMA);
    }

    @Test
    void testGetPeriodVolumeVWAP_inputTrades_expectVWAPs() {
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
        Map<Long, Map<String, BigDecimal>> resultVWMA = calculationEngine.calcIntervalVWAPs(testTrades, 10);
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
        assertEquals(new BigDecimal("133.33333334"), calculationEngine.calcIntervalVWAP(pricesPerInterval, volumesPerInterval));
    }

    @Test
    void testCalcIntervalVolumeSum_expectVolumeSum() {
        List<BigDecimal> volumesPerInterval = new ArrayList<>();
        volumesPerInterval.add(new BigDecimal("4"));
        volumesPerInterval.add(new BigDecimal("2"));
        assertEquals(new BigDecimal("6"), calculationEngine.calcVolumeSum(volumesPerInterval));
    }

}