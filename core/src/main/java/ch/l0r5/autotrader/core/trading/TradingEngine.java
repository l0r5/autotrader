package ch.l0r5.autotrader.core.trading;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.l0r5.autotrader.model.Trade;
import ch.l0r5.autotrader.model.enums.Type;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TradingEngine {

    public BigDecimal getSignal(Type tradingMode) {
        if (tradingMode == Type.BUY) return calcBuySignal();
        else return calcSellSignal();
    }

    private BigDecimal calcBuySignal() {
        return new BigDecimal("0");
    }

    private BigDecimal calcSellSignal() {
        return new BigDecimal("0");
    }

    protected Map<Long, Map<String, BigDecimal>> calcVWMA(List<Trade> trades, int intervalSeconds) {
        Map<Long, Map<String, BigDecimal>> resultVWAP = new HashMap<>();
        List<BigDecimal> pricesPerInterval = new ArrayList<>();
        List<BigDecimal> volumesPerInterval = new ArrayList<>();
        long currentTimeInterval = 0;
        for (int i = 0; i < trades.size(); i++) {
            Trade trade = trades.get(i);
            if (currentTimeInterval == 0) {
                currentTimeInterval = trade.getTime();
            }
            pricesPerInterval.add(trade.getPrice());
            volumesPerInterval.add(trade.getVolume());
            boolean isLastOfInterval = i == trades.size() - 1 || trades.get(i + 1).getTime() >= currentTimeInterval + intervalSeconds;
            if (isLastOfInterval) {
                currentTimeInterval = trade.getTime();
                calcIntervalVolumeSum(volumesPerInterval);
                calcIntervalVWAP(pricesPerInterval, volumesPerInterval);
                Map<String, BigDecimal> intervalPair = new HashMap<>();
                intervalPair.put("vwap", calcIntervalVWAP(pricesPerInterval, volumesPerInterval));
                intervalPair.put("volume", calcIntervalVolumeSum(volumesPerInterval));
                resultVWAP.put(currentTimeInterval, intervalPair);
                pricesPerInterval = new ArrayList<>();
                volumesPerInterval = new ArrayList<>();
            }
        }
        return resultVWAP;
    }

    protected BigDecimal calcIntervalVWAP(List<BigDecimal> pricesPerInterval, List<BigDecimal> volumesPerInterval) {
        BigDecimal volumePrice = new BigDecimal("0");
        for (int i = 0; i < pricesPerInterval.size(); i++) {
            BigDecimal price = pricesPerInterval.get(i);
            BigDecimal volume = volumesPerInterval.get(i);
            volumePrice = price.multiply(volume).add(volumePrice);
        }
        return volumePrice.divide(calcIntervalVolumeSum(volumesPerInterval), 8, BigDecimal.ROUND_UP);
    }

    protected BigDecimal calcIntervalVolumeSum(List<BigDecimal> volumes) {
        BigDecimal sumVolume = new BigDecimal("0");
        for (BigDecimal volume : volumes) {
            sumVolume = sumVolume.add(volume);
        }
        return sumVolume;
    }
}
