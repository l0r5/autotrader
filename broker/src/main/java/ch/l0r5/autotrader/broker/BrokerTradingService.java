package ch.l0r5.autotrader.broker;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import ch.l0r5.autotrader.model.Asset;
import ch.l0r5.autotrader.model.Order;
import ch.l0r5.autotrader.model.enums.OrderType;
import ch.l0r5.autotrader.model.enums.Type;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@Profile("!test")
public class BrokerTradingService implements TradingService {

    final Broker broker;

    public BrokerTradingService(Broker broker) {
        this.broker = broker;
    }

    @Override
    public void setStrategy() {
        log.info("Setting Strategy...");
    }

    @Override
    public void startTrading() {
        log.info("Start Trading...");
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

    @Override
    public void stopTrading() {
        log.info("Stopping Trading...");

    }
}
