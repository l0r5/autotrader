package ch.l0r5.autotrader.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.api.dto.OpenOrdersDto;
import ch.l0r5.autotrader.api.dto.TickerDto;
import ch.l0r5.autotrader.api.enums.Operation;
import ch.l0r5.autotrader.broker.models.Order;
import ch.l0r5.autotrader.utils.DataFormatUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KrakenPlatformController implements PlatformController {

    private final RestHandler restHandler;

    public KrakenPlatformController(RestHandler restHandler) {
        this.restHandler = restHandler;
    }

    @Override
    public BalanceDto getCurrentBalance() {
        BalanceDto balanceDto = new BalanceDto();
        try {
            balanceDto = DataFormatUtils.Json.fromJson(DataFormatUtils.Json.parse(requestCurrentBalance()), BalanceDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error during Balance update processing: ", e);
        }
        return balanceDto;
    }

    @Override
    public OpenOrdersDto getOpenOrders() {
        OpenOrdersDto openOrdersDto = new OpenOrdersDto();
        try {
            openOrdersDto = DataFormatUtils.Json.fromJson(DataFormatUtils.Json.parse(requestOpenOrders()).get("result"), OpenOrdersDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error during OpenOrders update processing: ", e);
        }
        return openOrdersDto;
    }

    @Override
    public void addOrder(Order order) {
        Map<String, String> qParams = new HashMap<>();
        qParams.put("pair", order.getPair());
        qParams.put("type", order.getType().getCode());
        qParams.put("ordertype", order.getOrderType().getCode());
        qParams.put("price", order.getPrice().toString());
        qParams.put("volume", order.getVolume().toString());
        if (order.getPrice2() != null) qParams.put("price2", order.getPrice2().toString());
        if (order.getLeverage() != null) qParams.put("leverage", order.getLeverage().toString());
        String path = "/0/private/" + Operation.ADD_ORDER.getCode();
        restHandler.makePostCall(qParams, path);
    }

    @Override
    public void cancelOpenOrder(String txId) {
        Map<String, String> qParams = Collections.singletonMap("txid", txId);
        String path = "/0/private/" + Operation.CANCEL_ORDER.getCode();
        restHandler.makePostCall(qParams, path);
    }

    @Override
    public TickerDto getTicker(String pair) {
        TickerDto tickerDto = new TickerDto();
        try {
            tickerDto = DataFormatUtils.Json.fromJson(DataFormatUtils.Json.parse(requestTicker(pair)).get("result").get(pair), TickerDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error during GetTicker processing: ", e);
        }
        return tickerDto;
    }

    private String requestCurrentBalance() {
        Map<String, String> qParams = Collections.singletonMap("asset", "xxbt");
        String path = "/0/private/" + Operation.BALANCE.getCode();
        return restHandler.makePostCall(qParams, path);
    }

    private String requestOpenOrders() {
        Map<String, String> qParams = new HashMap<>();
        String path = "/0/private/" + Operation.OPEN_ORDERS.getCode();
        return restHandler.makePostCall(qParams, path);
    }

    private String requestTicker(String pair) {
        Map<String, String> qParams = Collections.singletonMap("pair", pair);
        String path = "/0/public/" + Operation.TICKER.getCode();
        return restHandler.makePostCall(qParams, path);
    }

}
