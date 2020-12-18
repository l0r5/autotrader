package ch.l0r5.autotrader.trading;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import ch.l0r5.autotrader.api.controllers.PlatformController;
import ch.l0r5.autotrader.wallet.Balance;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class Trader {

    private final PlatformController platformController;
    private final Balance balance;
    private List<String> cryptoWatchList;

    public Trader(PlatformController platformController) {
        this.platformController = platformController;
        this.balance = new Balance();
        this.cryptoWatchList = new ArrayList<>();
    }

    public void updateBalances() {
        this.balance.setCurrentBalance(platformController.getCurrentBalance());
        log.info("Updated balance. New Balance: {}", this.balance.getCurrentBalance());
    }

    public void updatePrices() {
        //TODO: implement
    }


}
