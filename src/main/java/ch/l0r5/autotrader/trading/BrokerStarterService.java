package ch.l0r5.autotrader.trading;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class BrokerStarterService {

    final Broker broker;

    public BrokerStarterService(Broker broker) {
        this.broker = broker;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startTrader() {
//        trader.cancelOpenOrder("ABC");
//        Balance balance = trader.getBalance();

//        broker.updatePrices();
//        System.out.println();

    }

}
