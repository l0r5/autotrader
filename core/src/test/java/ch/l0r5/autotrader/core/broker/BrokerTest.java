package ch.l0r5.autotrader.core.broker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import ch.l0r5.autotrader.api.authentication.ApiAuthenticationHandler;
import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.model.Order;
import ch.l0r5.autotrader.model.enums.OrderType;
import ch.l0r5.autotrader.model.enums.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
class BrokerTest {

    @MockBean
    PlatformController platformController;

    @MockBean
    ApiAuthenticationHandler authenticationHandler;

    @Autowired
    Broker broker;

    @Test
    void testUpdateBalances_expectExecuteCall() {
        broker.setBalances(Collections.emptyMap());
        assertTrue(broker.getBalances().isEmpty());
        when(platformController.getCurrentBalance()).thenReturn(new BalanceDto(Collections.singletonMap("EUR", new BigDecimal("100"))));
        broker.updateBalances();
        verify(platformController, times(1)).getCurrentBalance();
        assertEquals(new BigDecimal("100"), broker.getBalances().get("EUR"));
    }

    @Test
    void testUpdateOpenOrders_withEmptyOrderDto_expectExecuteCall() {
        when(platformController.getOpenOrders()).thenReturn(Collections.emptyMap());
        broker.updateOpenOrders();
        verify(platformController, times(1)).getOpenOrders();
    }

    @Test
    void testUpdateOpenOrders_withFilledOrderDto_expectExecuteCall() {
        String txId = "ABC-123";
        Order testOrder = Order.builder()
                .txId(txId)
                .pair("ethchf")
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .price(new BigDecimal("123"))
                .build();
        Map<String, Order> testOpenOrders = Collections.singletonMap(txId, testOrder);
        when(platformController.getOpenOrders()).thenReturn(testOpenOrders);
        broker.updateOpenOrders();
        verify(platformController, times(1)).getOpenOrders();
        assertEquals("ethchf", broker.getOpenOrders().get(txId).getPair());
        assertEquals(Type.BUY, broker.getOpenOrders().get(txId).getType());
    }

    @Test
    void testPlaceOrder_expectNewOrder() {
        Order testOrder = Order.builder()
                .pair("xbtchf")
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .price(new BigDecimal("300.00"))
                .volume(new BigDecimal("1"))
                .build();
        broker.placeOrder(testOrder);
        verify(platformController, times(1)).addOrder(testOrder);
    }

    @Test
    void testCancelOpenOrder_expectOpenOrders() {
        String testTrxId = "test-trx-id";
        broker.cancelOpenOrder(testTrxId);
        verify(platformController, times(1)).cancelOpenOrder(testTrxId);
    }

    @Test
    void testUpdatePrices_pricesGetUpdated() {
//        String testPair = "ethchf";
//        TradesDto tradesDto = new TradesDto();
//        BigDecimal[] volWeightAverPriceArr = new BigDecimal[]{new BigDecimal("240"), new BigDecimal("260")};
//        tradesDto.setVolWeightAverPriceArr(volWeightAverPriceArr);
//        when(platformController.getRecentTrades(testPair)).thenReturn(tradesDto);
//        broker.setTradeAssets(Collections.singletonList(new Asset(testPair, new BigDecimal("0"))));
//        broker.updatePrices();
//        Asset asset = broker.getTradeAssets().stream().findFirst().get();
//        assertEquals(testPair, asset.getPair());
//        assertEquals(new BigDecimal("250"), asset.getPrice());
    }
}