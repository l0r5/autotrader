package ch.l0r5.autotrader.broker;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.api.dto.TickerDto;
import ch.l0r5.autotrader.broker.enums.OrderType;
import ch.l0r5.autotrader.broker.enums.Type;
import ch.l0r5.autotrader.broker.models.Asset;
import ch.l0r5.autotrader.broker.models.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Component
public class Broker {

    private final PlatformController platformController;
    private BalanceDto balanceDto;
    Map<String, Order> openOrders;
    private List<Asset> tradeAssets;


    public Broker(PlatformController platformController) {
        this.platformController = platformController;
        this.balanceDto = new BalanceDto();
        this.tradeAssets = new ArrayList<>();
        this.openOrders = new HashMap<>();
    }

    public void updateBalances() {
        this.balanceDto = platformController.getCurrentBalance();
        log.info("Updated balance. New Balance: {}", balanceDto.getCurrentBalance());
    }

    public void updateOpenOrders() {
        if (platformController.getOpenOrders() == null || platformController.getOpenOrders().getOrders() == null) {
            log.info("Updated OpenOrders: No update received.");
            return;
        }
        platformController.getOpenOrders().getOrders().forEach((txId, orderDto) -> {
            Order order = Order.builder()
                    .txId(txId)
                    .pair(orderDto.getDescr().get("pair"))
                    .type(Type.valueOfCode(orderDto.getDescr().get("type")))
                    .orderType(OrderType.valueOfCode(orderDto.getDescr().get("ordertype")))
                    .price(new BigDecimal(orderDto.getDescr().get("price")))
                    .price2(orderDto.getDescr().get("price2") != null ? new BigDecimal(orderDto.getDescr().get("price2")) : null)
                    .volume(orderDto.getDescr().get("volume") != null ? new BigDecimal(orderDto.getDescr().get("volume")) : null)
                    .leverage(orderDto.getDescr().get("leverage") != null && !orderDto.getDescr().get("leverage").equals("none") ? new BigDecimal(orderDto.getDescr().get("leverage")) : null)
                    .build();
            this.openOrders.put(txId, order);
        });
        log.info("Updated OpenOrders: {}", openOrders.toString());
    }

    public void updatePrices() {
        tradeAssets.forEach(asset -> {
            TickerDto tickerDto = platformController.getTicker(asset.getPair());
            asset.setPrice(tickerDto.getVolWeightAverPriceArr()[0].add(tickerDto.getVolWeightAverPriceArr()[1]).divide(new BigDecimal("2"), 2));
        });
        log.info("Updated Prices.");
    }

    public void placeOrder(Order order) {
        platformController.addOrder(order);
        log.info("Order was placed. Order: {}", order.toString());
    }

    public void cancelOpenOrder(String trxId) {
        platformController.cancelOpenOrder(trxId);
        log.info("Canceled OpenOrder with refId: {}", trxId);
    }

}
