package ch.l0r5.autotrader.broker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.api.dto.OpenOrdersDto;
import ch.l0r5.autotrader.broker.enums.OrderType;
import ch.l0r5.autotrader.broker.enums.Type;
import ch.l0r5.autotrader.broker.models.Order;

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
    void testUpdateOpenOrders_expectExecuteCall() {
        when(platformController.getOpenOrders()).thenReturn(new OpenOrdersDto());
        broker.updateOpenOrders();
        verify(platformController, times(1)).getOpenOrders();
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
}