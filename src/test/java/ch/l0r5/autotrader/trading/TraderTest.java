package ch.l0r5.autotrader.trading;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.l0r5.autotrader.api.authentication.ApiAuthenticationHandler;
import ch.l0r5.autotrader.api.controllers.PlatformController;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TraderTest {

    private Trader trader;

    @BeforeEach
    void setUp() {
        trader = new Trader(new PlatformController(new ApiAuthenticationHandler()));
    }

    @Test
    void testUpdateBalances_expectNotEmpty() {
        trader.updateBalances();
        assertFalse(trader.getBalance().getCurrentBalance().isEmpty());
    }

    @Test
    void testUpdateOpenOrders_expectNotEmpty() {
        trader.updateOpenOrders();
        assertNotNull(trader.getOpenOrders());
    }

}