package ch.l0r5.autotrader.model;

import java.math.BigDecimal;

import ch.l0r5.autotrader.model.enums.OrderType;
import ch.l0r5.autotrader.model.enums.Type;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Order {
    private final String txId;
    private final String pair;
    private final Type type;
    private final OrderType orderType;
    private final BigDecimal price;
    private final BigDecimal price2;
    private final BigDecimal volume;
    private final BigDecimal leverage;

}
