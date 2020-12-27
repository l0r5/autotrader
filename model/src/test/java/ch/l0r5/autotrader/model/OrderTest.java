package ch.l0r5.autotrader.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import ch.l0r5.autotrader.model.enums.OrderType;
import ch.l0r5.autotrader.model.enums.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .txId("123ABC")
                .pair("ethchf")
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .price(new BigDecimal("300"))
                .price2(new BigDecimal("400"))
                .volume(new BigDecimal("5000"))
                .leverage(new BigDecimal("3"))
                .build();
    }

    @Test
    void testAttributes() {
        assertEquals("123ABC", order.getTxId());
        assertEquals("ethchf", order.getPair());
        assertEquals(Type.BUY, order.getType());
        assertEquals(OrderType.LIMIT, order.getOrderType());
        assertEquals(new BigDecimal("300"), order.getPrice());
        assertEquals(new BigDecimal("400"), order.getPrice2());
        assertEquals(new BigDecimal("5000"), order.getVolume());
        assertEquals(new BigDecimal("3"), order.getLeverage());
        assertEquals(8, order.getClass().getDeclaredFields().length);
    }

    @Test
    void testAttributeEnums() {
        assertEquals("buy", order.getType().getCode());
        assertEquals(Type.BUY, Type.valueOfCode(order.getType().getCode()));
        assertNull(Type.valueOfCode("invalid"));
        assertEquals("limit", order.getOrderType().getCode());
        assertEquals(OrderType.LIMIT, OrderType.valueOfCode(order.getOrderType().getCode()));
        assertNull(OrderType.valueOfCode("invalid"));
    }

}