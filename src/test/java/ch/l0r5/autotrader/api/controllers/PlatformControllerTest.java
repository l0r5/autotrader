package ch.l0r5.autotrader.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.l0r5.autotrader.api.authentication.ApiAuthenticationHandler;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlatformControllerTest {

    private PlatformController platformController;

    @BeforeEach
    void setUp() {
        platformController = new PlatformController(new ApiAuthenticationHandler());
    }

    @Test
    void testGetCurrentBalance() {
        assertNotNull(platformController.getCurrentBalance());
    }

}