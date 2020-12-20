package ch.l0r5.autotrader.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.l0r5.autotrader.api.dto.Balance;
import ch.l0r5.autotrader.api.dto.OpenOrders;
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
    public Balance getCurrentBalance() {
        Balance balance = new Balance();
        try {
            balance = DataFormatUtils.Json.fromJson(DataFormatUtils.Json.parse(requestCurrentBalance()), Balance.class);
        } catch (JsonProcessingException e) {
            log.error("Error during Balance update processing: ", e);
        }
        return balance;
    }

    @Override
    public OpenOrders getOpenOrders() {
        OpenOrders openOrders = new OpenOrders();
        try {
            openOrders = DataFormatUtils.Json.fromJson(DataFormatUtils.Json.parse(requestOpenOrders()).get("result"), OpenOrders.class);
        } catch (JsonProcessingException e) {
            log.error("Error during OpenOrders update processing: ", e);
        }
        return openOrders;
    }

    @Override
    public void cancelOpenOrder(String txId) {
        Map<String, String> qParams = Collections.singletonMap("txid", txId);
        String path = "/0/private/" + Operation.CANCELORDER.getCode();
        restHandler.makePostCall(qParams, path);
    }

    private String requestCurrentBalance() {
        Map<String, String> qParams = Collections.singletonMap("asset", "xxbt");
        String path = "/0/private/" + Operation.BALANCE.getCode();
        return restHandler.makePostCall(qParams, path);
    }

    private String requestOpenOrders() {
        Map<String, String> qParams = new HashMap<>();
        String path = "/0/private/" + Operation.OPENORDERS.getCode();
        return restHandler.makePostCall(qParams, path);
    }

}
