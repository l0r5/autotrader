package ch.l0r5.autotrader.core.calculation;

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

    /**
     * This method returns an object that includes the vwap and vwma information of intervals
     * in a given period.
     *
     * @param trades         All trades in a given period
     * @param intervalSize   Size of the interval in seconds, e.g. one interval could last 10s
     * @param intervalNumber Number of intervals that are accounted in the calculation of the average
     * @return Map of volume, vwap and vwma per interval, ordered according to their interval time
     */
    public Map<Long, Map<String, BigDecimal>> getVWMAs(List<Trade> trades, int intervalSize, int intervalNumber) {
        return calcIntervalVWMAs(trades, intervalSize, intervalNumber);
    }

    protected Map<Long, Map<String, BigDecimal>> calcIntervalVWMAs(List<Trade> trades, int intervalSeconds, int intervalNumber) {
        Map<Long, Map<String, BigDecimal>> intervalSets = calcIntervalVWAPs(trades, intervalSeconds);
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
            intervalEntry.put("vwma", calcIntervalVWMAs(intervalVWAPsForVWMA, intervalVolumesForVWMA));
            intervalsToCheck.remove(max);
        }
        return intervalSets;
    }

    protected Map<Long, Map<String, BigDecimal>> calcIntervalVWAPs(List<Trade> trades, int intervalSeconds) {
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
                BigDecimal intervalVolumeSum = calcVolumeSum(volumesPerInterval);
                intervalPair.put("vwap", intervalVWAP);
                intervalPair.put("volume", intervalVolumeSum);
                intervalVWAPsVolumes.put(currentTimeInterval, intervalPair);
                pricesPerInterval = new ArrayList<>();
                volumesPerInterval = new ArrayList<>();
            }
        }
        return intervalVWAPsVolumes;
    }

    protected BigDecimal calcIntervalVWMAs(List<BigDecimal> vwaps, List<BigDecimal> volumes) {
        return calcAverage(vwaps, volumes);
    }

    protected BigDecimal calcIntervalVWAP(List<BigDecimal> pricesPerInterval, List<BigDecimal> volumesPerInterval) {
        return calcAverage(pricesPerInterval, volumesPerInterval);
    }

    private BigDecimal calcAverage(List<BigDecimal> prices, List<BigDecimal> volumes) {
        BigDecimal volumePrice = new BigDecimal("0");
        for (int i = 0; i < prices.size(); i++) {
            BigDecimal price = prices.get(i);
            BigDecimal volume = volumes.get(i);
            volumePrice = price.multiply(volume).add(volumePrice);
        }
        return volumePrice.divide(calcVolumeSum(volumes), 8, BigDecimal.ROUND_UP);
    }

    protected BigDecimal calcVolumeSum(List<BigDecimal> volumes) {
        BigDecimal sumVolume = new BigDecimal("0");
        for (BigDecimal volume : volumes) {
            sumVolume = sumVolume.add(volume);
        }
        return sumVolume;
    }
}
