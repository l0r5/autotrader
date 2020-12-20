package ch.l0r5.autotrader.trading;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.api.dto.Balance;
import ch.l0r5.autotrader.api.dto.OpenOrders;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class Trader {

    private final PlatformController platformController;
    private Balance balance;
    private List<String> cryptoWatchList;
    private OpenOrders openOrders;

    public Trader(PlatformController platformController) {
        this.platformController = platformController;
        this.balance = new Balance();
        this.cryptoWatchList = new ArrayList<>();
        this.openOrders = new OpenOrders();
    }

    public void updateBalances() {
        this.balance = platformController.getCurrentBalance();
        log.info("Updated balance. New Balance: {}", this.balance.getCurrentBalance());
    }

    public void updateOpenOrders() {
        this.openOrders = platformController.getOpenOrders();
        log.info("Updated OpenOrders: {}", openOrders.toString());
    }

    public void updatePrices() {
        //TODO: implement
    }

    public void cancelOpenOrder(String trxId) {
        platformController.cancelOpenOrder(trxId);
        log.info("Canceled OpenOrder with refId: {}", trxId);
    }


}
