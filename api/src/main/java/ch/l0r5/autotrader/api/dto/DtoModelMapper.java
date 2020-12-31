package ch.l0r5.autotrader.api.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.l0r5.autotrader.model.Order;
import ch.l0r5.autotrader.model.Trade;
import ch.l0r5.autotrader.model.enums.OrderType;
import ch.l0r5.autotrader.model.enums.Type;


public class DtoModelMapper {

    public static Map<String, Order> mapToOrders(OpenOrdersDto openOrders) {
        if (openOrders == null || openOrders.getOrders() == null) return Collections.emptyMap();
        Map<String, Order> resultOrders = new HashMap<>();
        openOrders.getOrders().forEach((txId, orderDto) -> resultOrders.put(txId, DtoModelMapper.mapToOrder(txId, orderDto)));
        return resultOrders;
    }

    private static Order mapToOrder(String txId, OrderDto orderDto) {
        return Order.builder()
                .txId(txId)
                .pair(orderDto.getDescr().get("pair"))
                .type(Type.valueOfCode(orderDto.getDescr().get("type")))
                .orderType(OrderType.valueOfCode(orderDto.getDescr().get("ordertype")))
                .price(new BigDecimal(orderDto.getDescr().get("price")))
                .price2(orderDto.getDescr().get("price2") != null ? new BigDecimal(orderDto.getDescr().get("price2")) : null)
                .volume(orderDto.getDescr().get("volume") != null ? new BigDecimal(orderDto.getDescr().get("volume")) : null)
                .leverage(orderDto.getDescr().get("leverage") != null && !orderDto.getDescr().get("leverage").equals("none") ? new BigDecimal(orderDto.getDescr().get("leverage")) : null)
                .build();
    }

    public static List<Trade> mapToTrade(TradeDto[] recentTradeDtos) {
        List<Trade> resultList = new ArrayList<>();
        for (TradeDto recentTradeDto : recentTradeDtos) {
            Type type = null;
            OrderType orderType = null;
            switch (recentTradeDto.getType()) {
                case "b":
                    type = Type.BUY;
                    break;
                case "s":
                    type = Type.SELL;
                    break;
            }

            switch (recentTradeDto.getOrderType()) {
                case "l":
                    orderType = OrderType.LIMIT;
                    break;
                case "m":
                    orderType = OrderType.MARKET;
                    break;
            }

            Trade trade = Trade.builder()
                    .price(recentTradeDto.getPrice())
                    .volume(recentTradeDto.getVolume())
                    .sinceTime(recentTradeDto.getTime())
                    .type(type)
                    .orderType(orderType)
                    .miscellaneous(recentTradeDto.getMiscellaneous())
                    .build();
            resultList.add(trade);
        }
        return resultList;
    }
}
