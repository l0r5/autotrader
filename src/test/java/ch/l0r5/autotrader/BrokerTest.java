package ch.l0r5.autotrader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.l0r5.autotrader.authentication.ApiAuthenticationHandler;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BrokerTest {

    private Broker broker;

    @BeforeEach
    void setUp() {
        broker = new Broker(new ApiAuthenticationHandler());
    }

    @Test
    void testGetCurrentBalance() {
        assertNotNull(broker.getCurrentBalance());
    }

}