package ch.l0r5.autotrader.broker;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.api.dto.OpenOrdersDto;
import ch.l0r5.autotrader.api.dto.TickerDto;
import ch.l0r5.autotrader.broker.models.Order;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class Broker {

    private final PlatformController platformController;
    private BalanceDto balanceDto;
    private List<TradeAsset> cryptoWatchList;
    private OpenOrdersDto openOrdersDto;

    public Broker(PlatformController platformController) {
        this.platformController = platformController;
        this.balanceDto = new BalanceDto();
        this.cryptoWatchList = new ArrayList<>();
        this.openOrdersDto = new OpenOrdersDto();
    }

    public void updateBalances() {
        this.balanceDto = platformController.getCurrentBalance();
        log.info("Updated balance. New Balance: {}", balanceDto.getCurrentBalance());
    }

    public void updateOpenOrders() {
        this.openOrdersDto = platformController.getOpenOrders();
        log.info("Updated OpenOrders: {}", openOrdersDto.toString());
    }

    public void updatePrices() {
        //TODO: implement
        String pair = "BTC/USD";
        TickerDto tickerDto = platformController.getTicker(pair);
        log.info("Updated Ticker for pair {}: {}", pair, tickerDto.toString());
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
