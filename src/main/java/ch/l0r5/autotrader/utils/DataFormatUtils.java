package ch.l0r5.autotrader.utils;

import java.util.Map;

public class DataFormatUtils {

    public static String getQueryString(Map<String, String> qParams) {
        StringBuilder result = new StringBuilder();
        qParams.forEach((k, v) -> result.append("&").append(k).append("=").append(v));
        return result.toString();
    }
}
