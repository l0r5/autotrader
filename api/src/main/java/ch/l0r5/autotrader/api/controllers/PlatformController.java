package ch.l0r5.autotrader.api.controllers;

import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.api.dto.OpenOrdersDto;
import ch.l0r5.autotrader.api.dto.TickerDto;
import ch.l0r5.autotrader.model.Order;

public interface PlatformController {

    BalanceDto getCurrentBalance();

    OpenOrdersDto getOpenOrders();

    void cancelOpenOrder(String refId);

    TickerDto getTicker(String pair);

    void addOrder(Order order);
}
