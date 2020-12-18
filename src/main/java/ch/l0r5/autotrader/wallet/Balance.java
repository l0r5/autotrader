package ch.l0r5.autotrader.wallet;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
public class Balance {

    Map<String, BigDecimal> currentBalance;

}
