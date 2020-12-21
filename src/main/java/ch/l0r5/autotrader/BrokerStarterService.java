package ch.l0r5.autotrader;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import ch.l0r5.autotrader.broker.Broker;
import ch.l0r5.autotrader.broker.enums.OrderType;
import ch.l0r5.autotrader.broker.enums.Type;
import ch.l0r5.autotrader.broker.models.Asset;
import ch.l0r5.autotrader.broker.models.Order;

@Service
@Profile("!test")
public class BrokerStarterService {

    final Broker broker;

    public BrokerStarterService(Broker broker) {
        this.broker = broker;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startTrader() {
        List<Asset> tradeAssets = Collections.singletonList(new Asset("ethchf", new BigDecimal("0.00")));
        Order limitOrder = Order.builder()
                .pair("xbtchf")
                .type(Type.BUY)
                .orderType(OrderType.LIMIT)
                .price(new BigDecimal("300.00"))
                .volume(new BigDecimal("1"))
                .build();

        broker.updateBalances();
        broker.updatePrices();
        broker.updateOpenOrders();
        broker.setTradeAssets(tradeAssets);
        broker.updatePrices();
        broker.placeOrder(limitOrder);
        broker.updateOpenOrders();
        broker.cancelAllOpenOrders();
        broker.updateOpenOrders();
    }
}
