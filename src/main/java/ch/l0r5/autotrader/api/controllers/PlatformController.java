package ch.l0r5.autotrader.api.controllers;

import ch.l0r5.autotrader.api.dto.Balance;
import ch.l0r5.autotrader.api.dto.OpenOrders;

public interface PlatformController {

    Balance getCurrentBalance();

    OpenOrders getOpenOrders();

    void cancelOpenOrder(String refId);
}
