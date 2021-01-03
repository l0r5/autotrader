package ch.l0r5.autotrader.application;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import ch.l0r5.autotrader.core.trading.TradingService;

@Service
@Profile("!test")
public class ApplicationRunner {

    final TradingService tradingService;

    public ApplicationRunner(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runApp() {
        tradingService.setStrategy();
        tradingService.startTrading();
        tradingService.stopTrading();
    }
}
