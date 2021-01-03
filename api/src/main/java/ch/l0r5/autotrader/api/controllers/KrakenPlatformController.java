package ch.l0r5.autotrader.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ch.l0r5.autotrader.api.dto.BalanceDto;
import ch.l0r5.autotrader.api.dto.DtoModelMapper;
import ch.l0r5.autotrader.api.dto.OpenOrdersDto;
import ch.l0r5.autotrader.api.dto.TradeDto;
import ch.l0r5.autotrader.api.dto.deserialize.Deserializer;
import ch.l0r5.autotrader.api.enums.Operation;
import ch.l0r5.autotrader.model.Order;
import ch.l0r5.autotrader.model.Trade;
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
            balanceDto = Deserializer.Json.fromJson(Deserializer.Json.parse(requestCurrentBalance()), BalanceDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error during Balance update processing: ", e);
        }
        return balanceDto;
    }

    @Override
    public Map<String, Order> getOpenOrders() {
        Map<String, Order> openOrders = Collections.emptyMap();
        try {
            OpenOrdersDto openOrdersDto = Deserializer.Json.fromJson(Deserializer.Json.parse(requestOpenOrders()).get("result"), OpenOrdersDto.class);
            openOrders = DtoModelMapper.mapToOrders(openOrdersDto);
        } catch (JsonProcessingException e) {
            log.error("Error during OpenOrders update processing: ", e);
        }
        return openOrders;
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
    public List<Trade> getTradesSince(String pair, long sinceTime) {
        List<Trade> allTrades = Collections.synchronizedList(new LinkedList<>());
        List<Trade> recentTrades = new ArrayList<>();
        long last = sinceTime;
        synchronized (allTrades) {
            do {
                try {
                    JsonNode jsonResponse = Deserializer.Json.parse(requestRecentTrades(pair, last));
                    if (!jsonResponse.get("error").isEmpty()) {
                        log.error("Error while fetching Trades: {}", jsonResponse.get("error").toString());
                        break;
                    }
                    last = Deserializer.Json.fromJson(jsonResponse.get("result").get("last"), Long.class);
                    TradeDto[] recentTradeDtos = Deserializer.Json.fromJson(jsonResponse.get("result").get(pair.toUpperCase(Locale.ROOT)), TradeDto[].class);
                    recentTrades = DtoModelMapper.mapToTrade(recentTradeDtos);
                    allTrades.wait(2000); // 1 call per second allowed;
                } catch (JsonProcessingException | InterruptedException e) {
                    log.error("Error during getRecentTrades processing: ", e);
                }
                allTrades.addAll(recentTrades);

            } while (recentTrades.size() == 1000);
        }
        return allTrades;
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

    private String requestRecentTrades(String pair, long sinceTime) {
        Map<String, String> qParams = new HashMap<>();
        qParams.put("pair", pair);
        qParams.put("since", String.valueOf(sinceTime));
        String path = "/0/public/" + Operation.TRADES.getCode();
        return restHandler.makePostCall(qParams, path);
    }

}
