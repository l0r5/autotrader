package ch.l0r5.autotrader.api.dto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.l0r5.autotrader.model.Order;
import ch.l0r5.autotrader.model.enums.OrderType;
import ch.l0r5.autotrader.model.enums.Type;


public class DtoMapper {

    public static Map<String, Order> mapToOrders(OpenOrdersDto openOrders) {
        if (openOrders == null || openOrders.getOrders() == null) return Collections.emptyMap();
        Map<String, Order> resultOrders = new HashMap<>();
        openOrders.getOrders().forEach((txId, orderDto) -> resultOrders.put(txId, DtoMapper.mapToOrder(txId, orderDto)));
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
}
