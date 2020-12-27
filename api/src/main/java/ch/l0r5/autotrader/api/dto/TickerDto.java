package ch.l0r5.autotrader.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerDto {

    @JsonAlias(value = "a")
    private BigDecimal[] askArr;

    @JsonAlias(value = "b")
    private BigDecimal[] bidArr;

    @JsonAlias(value = "c")
    private BigDecimal[] closedArr;

    @JsonAlias(value = "v")
    private BigDecimal[] volArr;

    @JsonAlias(value = "p")
    private BigDecimal[] volWeightAverPriceArr;

    @JsonAlias(value = "t")
    private long[] numberTraders;

    @JsonAlias(value = "l")
    private BigDecimal[] lowArr;

    @JsonAlias(value = "h")
    private BigDecimal[] highArr;

    @JsonAlias(value = "o")
    private BigDecimal openingPrice;
}
