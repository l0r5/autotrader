package ch.l0r5.autotrader.api.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.model.Order;
import ch.l0r5.autotrader.model.Trade;
import ch.l0r5.autotrader.model.enums.OrderType;
import ch.l0r5.autotrader.model.enums.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class KrakenPlatformControllerTest {

    @MockBean
    RestHandler restHandler;

    @Autowired
    KrakenPlatformController krakenPlatformController;

    @Test
    void testGetCurrentBalance_expectBalance() {
        String expectedString = "{ZEUR=4.5097, CHF=150.0000, KFEE=10500.00, XXBT=0.0000068549, XXRP=1.00000000, XLTC=1.0000000000, XETH=1.0000083700, GNO=0E-10, BCH=0.0000080700, BSV=0E-10}";
        String mockMessage = "{\"error\":[],\"result\":{\"ZEUR\":\"4.5097\",\"CHF\":\"150.0000\",\"KFEE\":\"10500.00\",\"XXBT\":\"0.0000068549\",\"XXRP\":\"1.00000000\",\"XLTC\":\"1.0000000000\",\"XETH\":\"1.0000083700\",\"GNO\":\"0.0000000000\",\"BCH\":\"0.0000080700\",\"BSV\":\"0.0000000000\"}}";
        when(restHandler.makePostCall(any(), any())).thenReturn(mockMessage);
        BalanceDto balanceDto = krakenPlatformController.getCurrentBalance();
        assertNotNull(balanceDto);
        assertEquals(10, balanceDto.getCurrentBalance().size());
        assertEquals(expectedString, balanceDto.getCurrentBalance().toString());
    }

    @Test
    void testGetOpenOrders_expectNoOpenOrders() {
        String expectedString = "{}";
        String mockMessage = "{\"error\":[],\"result\":{\"open\":{}}}";
        when(restHandler.makePostCall(any(), any())).thenReturn(mockMessage);
        Map<String, Order> orders = krakenPlatformController.getOpenOrders();
        assertNotNull(orders);
        assertEquals(0, orders.size());
        assertEquals(expectedString, orders.toString());
    }

    @Test
    void testGetOpenOrders_expectOneOpenOrders() {
        Order expectedOrder = Order.builder()
                .txId("O3HHYS-7XXNS-ST655X")
                .pair("XBTCHF")
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .price(new BigDecimal("3.0"))
                .price2(new BigDecimal("0"))
                .build();
        String mockMessage = "{\"error\":[],\"result\":{\"open\":{\"O3HHYS-7XXNS-ST655X\":{\"refid\":null,\"userref\":0,\"status\":\"open\",\"opentm\":1608500588.7058,\"starttm\":0,\"expiretm\":0,\"descr\":{\"pair\":\"XBTCHF\",\"type\":\"buy\",\"ordertype\":\"limit\",\"price\":\"3.0\",\"price2\":\"0\",\"leverage\":\"none\",\"order\":\"buy 1.00000000 XBTCHF @ limit 3.0\",\"close\":\"\"},\"vol\":\"1.00000000\",\"vol_exec\":\"0.00000000\",\"cost\":\"0.00000\",\"fee\":\"0.00000\",\"price\":\"0.00000\",\"stopprice\":\"0.00000\",\"limitprice\":\"0.00000\",\"misc\":\"\",\"oflags\":\"fciq\"}}}}";
        when(restHandler.makePostCall(any(), any())).thenReturn(mockMessage);
        Map<String, Order> orders = krakenPlatformController.getOpenOrders();
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertTrue(new ReflectionEquals(expectedOrder).matches(orders.get("O3HHYS-7XXNS-ST655X")));
    }

    @Test
    void testCancelOpenOrder_expectOpenOrderToBeCanceled() {
        String txId = "ABCD-1234-ABC123";
        String mockMessage = "{\"error\":[],\"result\":{\"count\":1}}";
        when(restHandler.makePostCall(any(), any())).thenReturn(mockMessage);
        krakenPlatformController.cancelOpenOrder(txId);
        verify(restHandler, times(1)).makePostCall(Collections.singletonMap("txid", txId), "/0/private/CancelOrder");
    }

    @Test
    void testGetRecentTrades_expectTrades() {
        String pair = "ethchf";
        long sinceTime = new Date().getTime();
        String mockMessage = "{\"error\":[],\"result\":{\"ETHCHF\":[[\"642.28000\",\"0.02937067\",1609264847.4769,\"b\",\"l\",\"test\"],[\"642.28000\",\"0.08179845\",1609264850.5637,\"b\",\"l\",\"\"],[\"642.28000\",\"0.00013067\",1609264850.5654,\"b\",\"l\",\"\"],[\"642.28000\",\"0.00000021\",1609264850.5669,\"b\",\"l\",\"\"]],\"last\":\"1609264850566852989\"}}";
        when(restHandler.makePostCall(any(), any())).thenReturn(mockMessage);
        List<Trade> recentTrades = krakenPlatformController.getRecentTrades(pair, sinceTime);
        assertNotNull(recentTrades);
        assertTrue(recentTrades.stream().findFirst().isPresent());
        assertEquals(new BigDecimal("642.28000"), recentTrades.stream().findFirst().get().getPrice());
        assertEquals(new BigDecimal("0.02937067"), recentTrades.stream().findFirst().get().getVolume());
        assertEquals(1.609264847E9, recentTrades.stream().findFirst().get().getTime());
        assertEquals(Type.BUY, recentTrades.stream().findFirst().get().getType());
        assertEquals(OrderType.LIMIT, recentTrades.stream().findFirst().get().getOrderType());
        assertEquals("test", recentTrades.stream().findFirst().get().getMiscellaneous());
    }

    @Test
    void testAddOrder_expectNewOrder() {
        Order testOrder = Order.builder()
                .pair("xbtchf")
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .price(new BigDecimal("300.00"))
                .volume(new BigDecimal("1"))
                .build();
        Map<String, String> qParams = new HashMap<>();
        qParams.put("pair", "xbtchf");
        qParams.put("type", "buy");
        qParams.put("ordertype", "limit");
        qParams.put("price", "300.00");
        qParams.put("volume", "1");
        String mockMessage = "{\"error\":[],\"result\":{\"descr\":{\"order\":\"buy 1.00000000 XBTCHF @ limit 300.0\"},\"txid\":[\"O2OYMV-ZUGMV-7J2DQK\"]}}";
        when(restHandler.makePostCall(any(), any())).thenReturn(mockMessage);
        krakenPlatformController.addOrder(testOrder);
        verify(restHandler, times(1)).makePostCall(qParams, "/0/private/AddOrder");
    }
}