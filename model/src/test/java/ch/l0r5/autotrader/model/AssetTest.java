package ch.l0r5.autotrader.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssetTest {

    private Asset asset;

    @BeforeEach
    void setUp() {
        String pair = "ethchf";
        BigDecimal price = new BigDecimal("300");
        asset = new Asset(pair, price);
    }

    @Test
    void testAttributes() {
        asset.setPrice(new BigDecimal("200"));
        assertEquals("ethchf", asset.getPair());
        assertEquals(new BigDecimal("200"), asset.getPrice());
        assertEquals(2, asset.getClass().getDeclaredFields().length);
    }

}