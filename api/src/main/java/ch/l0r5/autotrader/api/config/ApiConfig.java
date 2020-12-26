package ch.l0r5.autotrader.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class ApiConfig {

    @Value("${API.KRAKEN.BASEURL}")
    private String baseUrl;

    @Value("${API.PRIVATE.KEY.LOCATION}")
    private String privateKeyLocation;

    @Value("${API.PUBLIC.KEY.LOCATION}")
    private String publicKeyLocation;

}
