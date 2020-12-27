package ch.l0r5.autotrader.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {

    @JsonAlias(value = "refid")
    private String refId;

    @JsonAlias(value = "userref")
    private int userRef;

    private String status;

    @JsonAlias(value = "opentm")
    private double openTm;

    @JsonAlias(value = "starttm")
    private int startTm;

    @JsonAlias(value = "expiretm")
    private int expireTm;

    private Map<String, String> descr;

    private BigDecimal vol;

    @JsonAlias(value = "vol_exec")
    private BigDecimal volExec;

    private BigDecimal cost;
    private BigDecimal fee;
    private BigDecimal price;

    @JsonAlias(value = "stopprice")
    private BigDecimal stopPrice;

    @JsonAlias(value = "limitprice")
    private BigDecimal limitPrice;

    private String misc;

    private String oflags;

}
