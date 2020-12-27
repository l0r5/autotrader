package ch.l0r5.autotrader;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import ch.l0r5.autotrader.api.authentication.ApiAuthenticationHandler;

@SpringBootTest
@ActiveProfiles("test")
class BrokerApplicationTests {

	@MockBean
	ApiAuthenticationHandler authenticationHandler;

	@Test
	void contextLoads() {
	}

}
