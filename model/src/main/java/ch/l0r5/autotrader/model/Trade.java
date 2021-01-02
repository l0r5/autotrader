package ch.l0r5.autotrader.model;


import java.math.BigDecimal;

import ch.l0r5.autotrader.model.enums.OrderType;
import ch.l0r5.autotrader.model.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Trade {
    private final BigDecimal price;
    private final BigDecimal volume;
    private final long time;
    private final Type type;
    private final OrderType orderType;
    private final String miscellaneous;
}
