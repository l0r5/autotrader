package ch.l0r5.autotrader.api.controllers;

import java.util.Map;

import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.api.dto.TickerDto;
import ch.l0r5.autotrader.model.Order;

public interface PlatformController {

    BalanceDto getCurrentBalance();

    Map<String, Order> getOpenOrders();

    void cancelOpenOrder(String refId);

    TickerDto getTicker(String pair);

    void addOrder(Order order);
}
