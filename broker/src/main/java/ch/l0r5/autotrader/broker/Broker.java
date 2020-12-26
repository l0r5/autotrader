package ch.l0r5.autotrader.broker;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.api.dto.DtoMapper;
import ch.l0r5.autotrader.api.dto.TickerDto;
import ch.l0r5.autotrader.model.Asset;
import ch.l0r5.autotrader.model.Order;
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
    private List<Asset> tradeAssets;

    public Broker(PlatformController platformController) {
        this.platformController = platformController;
        this.balances = new HashMap<>();
        this.tradeAssets = new ArrayList<>();
        this.openOrders = new HashMap<>();
    }

    public void updateBalances() {
        log.info("Updating Balances...");
        this.balances = platformController.getCurrentBalance().getCurrentBalance();
        log.info("Updated balance. New Balance: {}", balances.toString());
    }

    public void updateOpenOrders() {
        log.info("Updating OpenOrders...");
        this.openOrders = DtoMapper.mapToOrders(platformController.getOpenOrders());
        log.info("Updated OpenOrders: {}", openOrders.toString());
    }

    public void updatePrices() {
        log.info("Updating Prices...");
        if (tradeAssets.isEmpty()) {
            log.info("No TradeAssets to update prices.");
            return;
        }
        tradeAssets.forEach(asset -> {
            TickerDto tickerDto = platformController.getTicker(asset.getPair());
            BigDecimal price = tickerDto.getVolWeightAverPriceArr()[0].add(tickerDto.getVolWeightAverPriceArr()[1]).divide(new BigDecimal("2"), 2);
            asset.setPrice(price);
            log.info("Updated Asset: {} with Price: {}.", asset.getPair(), asset.getPrice());
        });
        log.info("Updated Prices.");
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
