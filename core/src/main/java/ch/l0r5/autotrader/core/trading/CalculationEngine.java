package ch.l0r5.autotrader.core.trading;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.l0r5.autotrader.model.Trade;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CalculationEngine {

    public Map<Long, Map<String, BigDecimal>> getVWMAs(List<Trade> trades, int intervalSeconds, int intervalNumber) {
        return calcVWMAsforPeriod(trades, intervalSeconds, intervalNumber);
    }

    protected Map<Long, Map<String, BigDecimal>> calcVWMAsforPeriod(List<Trade> trades, int intervalSeconds, int intervalNumber) {
        Map<Long, Map<String, BigDecimal>> intervalSets = getPeriodVolumeVWAP(trades, intervalSeconds);
        Map<Long, Map<String, BigDecimal>> intervalsToCheck = new HashMap<>(intervalSets);
        while (intervalsToCheck.size() > 0) {
            Map<Long, Map<String, BigDecimal>> periodIntervalsToCheck = new HashMap<>(intervalsToCheck);
            List<BigDecimal> intervalVWAPsForVWMA = new ArrayList<>();
            List<BigDecimal> intervalVolumesForVWMA = new ArrayList<>();
            long max = 0;
            for (int i = 0; i < intervalNumber; i++) {
                if (periodIntervalsToCheck.isEmpty()) break;
                long localMax = Collections.max(periodIntervalsToCheck.keySet());
                if (max == 0) max = localMax;
                intervalVWAPsForVWMA.add(intervalsToCheck.get(localMax).get("vwap"));
                intervalVolumesForVWMA.add(intervalsToCheck.get(localMax).get("volume"));
                periodIntervalsToCheck.remove(localMax);
            }
            Map<String, BigDecimal> intervalEntry = intervalSets.get(max);
            intervalEntry.put("vwma", calcVWMAsforPeriod(intervalVWAPsForVWMA, intervalVolumesForVWMA));
            intervalsToCheck.remove(max);
        }
        return intervalSets;
    }

    protected BigDecimal calcVWMAsforPeriod(List<BigDecimal> vwaps, List<BigDecimal> volumes) {
        BigDecimal volumePrice = new BigDecimal("0");
        for (int i = 0; i < vwaps.size(); i++) {
            BigDecimal price = vwaps.get(i);
            BigDecimal volume = volumes.get(i);
            volumePrice = price.multiply(volume).add(volumePrice);
        }
        return volumePrice.divide(calcIntervalVolumeSum(volumes), 8, BigDecimal.ROUND_UP);
    }

    protected Map<Long, Map<String, BigDecimal>> getPeriodVolumeVWAP(List<Trade> trades, int intervalSeconds) {
        Map<Long, Map<String, BigDecimal>> intervalVWAPsVolumes = new HashMap<>();
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
                Map<String, BigDecimal> intervalPair = new HashMap<>();
                BigDecimal intervalVWAP = calcIntervalVWAP(pricesPerInterval, volumesPerInterval);
                BigDecimal intervalVolumeSum = calcIntervalVolumeSum(volumesPerInterval);
                intervalPair.put("vwap", intervalVWAP);
                intervalPair.put("volume", intervalVolumeSum);
                intervalVWAPsVolumes.put(currentTimeInterval, intervalPair);
                pricesPerInterval = new ArrayList<>();
                volumesPerInterval = new ArrayList<>();
            }
        }
        return intervalVWAPsVolumes;
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
