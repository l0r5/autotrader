package ch.l0r5.autotrader.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Asset {

    private String pair;
    private BigDecimal price;

}
