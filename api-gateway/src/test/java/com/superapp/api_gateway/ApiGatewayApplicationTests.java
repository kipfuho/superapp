package com.superapp.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiGatewayApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void contextLoads() {
		// basic sanity check
	}

	@Test
	void actuatorShouldBeExposed() {
		webTestClient.get()
				.uri("http://localhost:" + port + "/actuator/health")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.status").isEqualTo("UP");
	}

	@Test
	void routesEndpointShouldListConfiguredRoutes() {
		webTestClient.get()
				.uri("http://localhost:" + port + "/actuator/gateway/routes")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray();
	}
}
