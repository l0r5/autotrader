package ch.l0r5.autotrader.trading;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.api.dto.Balance;
import ch.l0r5.autotrader.api.dto.OpenOrders;

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
        when(platformController.getCurrentBalance()).thenReturn(new Balance());
        broker.updateBalances();
        verify(platformController, times(1)).getCurrentBalance();
    }

    @Test
    void testUpdateOpenOrders_expectExecuteCall() {
        when(platformController.getOpenOrders()).thenReturn(new OpenOrders());
        broker.updateOpenOrders();
        verify(platformController, times(1)).getOpenOrders();
    }

    @Test
    void testCancelOpenOrder_expectOpenOrders() {
        String testTrxId = "test-trx-id";
        broker.cancelOpenOrder(testTrxId);
        verify(platformController, times(1)).cancelOpenOrder(testTrxId);
    }
}