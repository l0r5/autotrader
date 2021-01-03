package ch.l0r5.autotrader.core.trading;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import ch.l0r5.autotrader.core.broker.Broker;
import ch.l0r5.autotrader.core.calculation.CalculationEngine;
import ch.l0r5.autotrader.model.Asset;
import ch.l0r5.autotrader.model.enums.Type;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@Profile("!test")
public class TradingServiceImpl implements TradingService {

    final Broker broker;

    public TradingServiceImpl(Broker broker) {
        this.broker = broker;
    }

    @Override
    public void setStrategy() {
        log.info("Setting Strategy...");
    }

    @Override
    public void startTrading() {
        log.info("Start Trading...");
//        Asset tradeAsset = new Asset("ethchf", new BigDecimal("0.00"));
//        Order limitOrder = Order.builder()
//                .pair("xbtchf")
//                .type(Type.BUY)
//                .orderType(OrderType.LIMIT)
//                .price(new BigDecimal("300.00"))
//                .volume(new BigDecimal("1"))
//                .build();

//        broker.updateBalances();
//        broker.updateAllTrades(1609264800);
//        broker.updateOpenOrders();
//        broker.setTradeAsset(tradeAsset);
//        broker.updateAllTrades(1609264900);
//        broker.placeOrder(limitOrder);
//        broker.updateOpenOrders();
//        broker.cancelAllOpenOrders();
//        broker.updateOpenOrders();

        initBroker(new BigDecimal("200"), new Asset("ethchf", new BigDecimal("0.00")));
        startTradingRoutine();
    }

    private void initBroker(BigDecimal tradingBalance, Asset asset) {
        broker.setTradingBalance(tradingBalance);
        broker.setTradeAsset(asset);
        broker.updateBalances();
        log.info("Initialized Broker with Trading Balance: {}, Trade Asset: {}", tradingBalance, asset);
    }

    private void startTradingRoutine() {
        Type tradingMode = Type.BUY;
        CalculationEngine calculationEngine = new CalculationEngine();
        broker.updateAllTrades(1609679086);
        Map<Long, Map<String, BigDecimal>> vwmas = calculationEngine.getVWMAs(broker.getAllTrades(), 60, 3);


        // init (buy) routine
        // checkPrices
        // calcSignals (buySignal)     => create ch.l0r5.autotrader.strategy.SignalCalculator.calcSignals()
        // if signal == true
        // placeOrder
        // break

        // sell routine
        // check prices
        // calcSignals (sellSignal)
        // if signal == true
        // placeOrder
        // break

        // switch to buy ...

        // if balance <= allowedValue -> SecurityCancel

        // stopTrading

    }

    @Override
    public void stopTrading() {
        log.info("Stopping Trading...");
    }
}
