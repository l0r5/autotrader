package ch.l0r5.autotrader.trading;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.api.dto.Balance;
import ch.l0r5.autotrader.api.dto.OpenOrders;
import ch.l0r5.autotrader.api.dto.Ticker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class Broker {

    private final PlatformController platformController;
    private Balance balance;
    private List<TradeAsset> cryptoWatchList;
    private OpenOrders openOrders;

    public Broker(PlatformController platformController) {
        this.platformController = platformController;
        this.balance = new Balance();
        this.cryptoWatchList = new ArrayList<>();
        this.openOrders = new OpenOrders();
    }

    public void updateBalances() {
        this.balance = platformController.getCurrentBalance();
        log.info("Updated balance. New Balance: {}", balance.getCurrentBalance());
    }

    public void updateOpenOrders() {
        this.openOrders = platformController.getOpenOrders();
        log.info("Updated OpenOrders: {}", openOrders.toString());
    }

    public void updatePrices() {
        //TODO: implement
        String pair = "BTC/USD";
        Ticker ticker = platformController.getTicker(pair);
        log.info("Updated Ticker for pair {}: {}", pair, ticker.toString());
    }

    public void cancelOpenOrder(String trxId) {
        platformController.cancelOpenOrder(trxId);
        log.info("Canceled OpenOrder with refId: {}", trxId);
    }


}
