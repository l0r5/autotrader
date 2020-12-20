package ch.l0r5.autotrader.trading;

import org.springframework.stereotype.Service;

@Service
public class TradingStarterServer {

    final Trader trader;

    public TradingStarterServer(Trader trader) {
        this.trader = trader;
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void startTrader() {
//        trader.cancelOpenOrder("ABC");
////        Balance balance = trader.getBalance();
//        System.out.println();
//
//
//    }

}
