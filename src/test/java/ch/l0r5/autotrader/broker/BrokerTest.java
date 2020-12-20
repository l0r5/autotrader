package ch.l0r5.autotrader.broker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.api.dto.OpenOrdersDto;
import ch.l0r5.autotrader.api.dto.OrderDto;
import ch.l0r5.autotrader.api.dto.TickerDto;
import ch.l0r5.autotrader.broker.enums.OrderType;
import ch.l0r5.autotrader.broker.enums.Type;
import ch.l0r5.autotrader.broker.models.Asset;
import ch.l0r5.autotrader.broker.models.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
class BrokerTest {

    @MockBean
    PlatformController platformController;

    @Autowired
    Broker broker;

    @Test
    void testUpdateBalances_expectExecuteCall() {
        when(platformController.getCurrentBalance()).thenReturn(new BalanceDto());
        broker.updateBalances();
        verify(platformController, times(1)).getCurrentBalance();
    }

    @Test
    void testUpdateOpenOrders_withEmptyOrderDto_expectExecuteCall() {
        when(platformController.getOpenOrders()).thenReturn(new OpenOrdersDto());
        broker.updateOpenOrders();
        verify(platformController, times(2)).getOpenOrders();
    }

    @Test
    void testUpdateOpenOrders_withFilledOrderDto_expectExecuteCall() {
        String txId = "ABC-123";
        OpenOrdersDto testOpenOrder = new OpenOrdersDto();
        Map<String, String> descr = new HashMap<>();
        descr.put("pair", "ethchf");
        descr.put("type", "buy");
        descr.put("ordertype", "limit");
        descr.put("price", "123");
        OrderDto orderDto = new OrderDto();
        orderDto.setDescr(descr);
        testOpenOrder.setOrders(Collections.singletonMap(txId, orderDto));
        when(platformController.getOpenOrders()).thenReturn(testOpenOrder);
        broker.updateOpenOrders();
        verify(platformController, times(3)).getOpenOrders();
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
        String testPair = "ethchf";
        TickerDto tickerDto = new TickerDto();
        BigDecimal[] volWeightAverPriceArr = new BigDecimal[]{new BigDecimal("240"), new BigDecimal("260")};
        tickerDto.setVolWeightAverPriceArr(volWeightAverPriceArr);
        when(platformController.getTicker(testPair)).thenReturn(tickerDto);
        broker.setTradeAssets(Collections.singletonList(new Asset(testPair, new BigDecimal("0"))));
        broker.updatePrices();
        Asset asset = broker.getTradeAssets().stream().findFirst().get();
        assertEquals(testPair, asset.getPair());
        assertEquals(new BigDecimal("250"), asset.getPrice());
    }
}