package ch.l0r5.autotrader.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {

    @JsonAlias(value = "result")
    private Map<String, BigDecimal> currentBalance;

}
