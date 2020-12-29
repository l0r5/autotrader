package ch.l0r5.autotrader.api.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

import lombok.Data;

@Data
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class TradeDto {

    private BigDecimal price;
    private BigDecimal volume;
    private long time;
    private String type;
    private String orderType;
    private String miscellaneous;
}
