package ch.l0r5.autotrader.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import ch.l0r5.autotrader.model.enums.OrderType;
import ch.l0r5.autotrader.model.enums.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TradeTest {

    private Trade trade;

    @BeforeEach
    void setUp() {
        trade = Trade.builder()
                .price(new BigDecimal("300"))
                .volume(new BigDecimal("5000"))
                .sinceTime(1609264800L)
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .miscellaneous("test")
                .build();
    }

    @Test
    void testAttributes() {
        assertEquals(new BigDecimal("300"), trade.getPrice());
        assertEquals(new BigDecimal("5000"), trade.getVolume());
        assertEquals(1609264800L, trade.getSinceTime());
        assertEquals(Type.BUY, trade.getType());
        assertEquals(OrderType.LIMIT, trade.getOrderType());
        assertEquals("test", trade.getMiscellaneous());
        assertEquals(6, trade.getClass().getDeclaredFields().length);
    }
}