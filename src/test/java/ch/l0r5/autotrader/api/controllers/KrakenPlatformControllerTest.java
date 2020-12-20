package ch.l0r5.autotrader.api.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;

import ch.l0r5.autotrader.api.dto.Balance;
import ch.l0r5.autotrader.api.dto.OpenOrders;
import ch.l0r5.autotrader.api.dto.Ticker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        Balance balance = krakenPlatformController.getCurrentBalance();
        assertNotNull(balance);
        assertEquals(10, balance.getCurrentBalance().size());
        assertEquals(expectedString, balance.getCurrentBalance().toString());
    }

    @Test
    void testGetOpenOrders_expectNoOpenOrders() {
        String expectedString = "{}";
        String mockMessage = "{\"error\":[],\"result\":{\"open\":{}}}";
        when(restHandler.makePostCall(any(), any())).thenReturn(mockMessage);
        OpenOrders orders = krakenPlatformController.getOpenOrders();
        assertNotNull(orders);
        assertEquals(0, orders.getOrders().size());
        assertEquals(expectedString, orders.getOrders().toString());
    }

    @Test
    void testGetOpenOrders_expectOneOpenOrders() {
        String expectedString = "{ABCD-1234-ABC123=Order(refId=null, userRef=0, status=open, openTm=123.123, startTm=0, expireTm=0, descr={pair=XBTCHF, type=buy, ordertype=limit, price=300.0, price2=0, leverage=none, order=buy 1.00000000 XBTCHF @ limit 300.0, close=}, vol=1.00000000, volExec=0E-8, cost=0.00000, fee=0.00000, price=0.00000, stopPrice=0.00000, limitPrice=0.00000, misc=, oflags=fciq)}";
        String mockMessage = "{\"error\":[],\"result\":{\"open\":{\"ABCD-1234-ABC123\":{\"refid\":null,\"userref\":0,\"status\":\"open\",\"opentm\":123.123,\"starttm\":0,\"expiretm\":0,\"descr\":{\"pair\":\"XBTCHF\",\"type\":\"buy\",\"ordertype\":\"limit\",\"price\":\"300.0\",\"price2\":\"0\",\"leverage\":\"none\",\"order\":\"buy 1.00000000 XBTCHF @ limit 300.0\",\"close\":\"\"},\"vol\":\"1.00000000\",\"vol_exec\":\"0.00000000\",\"cost\":\"0.00000\",\"fee\":\"0.00000\",\"price\":\"0.00000\",\"stopprice\":\"0.00000\",\"limitprice\":\"0.00000\",\"misc\":\"\",\"oflags\":\"fciq\"}}}}";
        when(restHandler.makePostCall(any(), any())).thenReturn(mockMessage);
        OpenOrders orders = krakenPlatformController.getOpenOrders();
        assertNotNull(orders);
        assertEquals(1, orders.getOrders().size());
        assertEquals(expectedString, orders.getOrders().toString());
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
    void testGetTicker_expectTicker() {
        String pair = "BTC/USD";
        String expectedString = "Ticker(askArr=[23575.10000, 8, 8.000], bidArr=[23575.00000, 1, 1.000], closedArr=[23575.00000, 0.00297300], volArr=[2821.04995637, 6401.25971027], numberTraders=[15835, 33920], lowArr=[23084.90000, 23084.90000], highArr=[23871.50000, 24288.20000], openingPrice=23871.50000)";
        String mockMessage = "{\"error\":[],\"result\":{\"BTC\\/USD\":{\"a\":[\"23575.10000\",\"8\",\"8.000\"],\"b\":[\"23575.00000\",\"1\",\"1.000\"],\"c\":[\"23575.00000\",\"0.00297300\"],\"v\":[\"2821.04995637\",\"6401.25971027\"],\"p\":[\"23473.17484\",\"23687.52161\"],\"t\":[15835,33920],\"l\":[\"23084.90000\",\"23084.90000\"],\"h\":[\"23871.50000\",\"24288.20000\"],\"o\":\"23871.50000\"}}}";
        when(restHandler.makePostCall(any(), any())).thenReturn(mockMessage);
        Ticker ticker = krakenPlatformController.getTicker(pair);
        assertNotNull(ticker);
        assertEquals(expectedString, ticker.toString());
        assertEquals(new BigDecimal("23575.10000"), ticker.getAskArr()[0]);
    }
}