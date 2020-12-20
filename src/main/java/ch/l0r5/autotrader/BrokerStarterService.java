package ch.l0r5.autotrader;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import ch.l0r5.autotrader.broker.Broker;

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

//        Order limitOrder = Order.builder()
//                .pair("xbtchf")
//                .type(Type.BUY)
//                .orderType(OrderType.LIMIT)
//                .price(new BigDecimal("300.00"))
//                .volume(new BigDecimal("1"))
//                .build();
//        broker.placeOrder(limitOrder);
//        broker.updateOpenOrders();

    }

}
