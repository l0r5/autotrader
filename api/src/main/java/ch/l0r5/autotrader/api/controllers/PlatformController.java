package ch.l0r5.autotrader.api.controllers;

import java.util.List;
import java.util.Map;

import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.model.Order;
import ch.l0r5.autotrader.model.Trade;

public interface PlatformController {

    BalanceDto getCurrentBalance();

    Map<String, Order> getOpenOrders();

    void cancelOpenOrder(String refId);

    List<Trade> getRecentTrades(String pair, long sinceTime);

    void addOrder(Order order);
}
