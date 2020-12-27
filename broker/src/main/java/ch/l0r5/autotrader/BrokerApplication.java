package ch.l0r5.autotrader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources({
		@PropertySource("classpath:application.properties"),
		@PropertySource("classpath:api.properties")
})
@SpringBootApplication
public class BrokerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrokerApplication.class, args);
	}
}
