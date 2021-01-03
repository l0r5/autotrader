package ch.l0r5.autotrader.core.broker;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.model.Asset;
import ch.l0r5.autotrader.model.Order;
import ch.l0r5.autotrader.model.Trade;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Component
public class Broker {

    private final PlatformController platformController;
    private Map<String, BigDecimal> balances;
    private Map<String, Order> openOrders;
    private Asset tradeAsset;
    private BigDecimal tradingBalance;
    private List<Trade> allTrades;

    public Broker(PlatformController platformController) {
        this.platformController = platformController;
        this.balances = new HashMap<>();
        this.tradeAsset = new Asset("", new BigDecimal("0"));
        this.openOrders = new HashMap<>();
        this.tradingBalance = new BigDecimal("0");
        this.allTrades = new ArrayList<>();
    }

    public void updateBalances() {
        log.info("Updating Balances...");
        this.balances = platformController.getCurrentBalance().getCurrentBalance();
        log.info("Updated balance. New Balance: {}", balances.toString());
    }

    public void updateOpenOrders() {
        log.info("Updating OpenOrders...");
        this.openOrders = platformController.getOpenOrders();
        log.info("Updated OpenOrders: {}", openOrders.toString());
    }

    public void updateAllTrades(long sinceTime) {
        log.info("Updating AllTrades list...");
        if (tradeAsset.getPair().isEmpty()) {
            log.info("No TradeAssets to update Trades for.");
            return;
        }
        List<Trade> recentTrades = platformController.getTradesSince(tradeAsset.getPair(), sinceTime);
        this.allTrades = Stream
                .concat(allTrades.stream(), recentTrades.stream())
                .collect(Collectors.toList());
        log.info("Updated AllTrades list.");
    }

    public void placeOrder(Order order) {
        log.info("Placing Order...");
        platformController.addOrder(order);
        log.info("Order was placed. Order: {}", order.toString());
    }

    public void cancelOpenOrder(String txId) {
        log.info("Canceling Order with txId: {} ...", txId);
        platformController.cancelOpenOrder(txId);
        log.info("Canceled OpenOrder with refId: {}", txId);
    }

    public void cancelAllOpenOrders() {
        log.info("Canceling all Open Orders...");
        openOrders.forEach((txId, order) -> platformController.cancelOpenOrder(txId));
        log.info("Canceled all Open Orders.");
    }
}
