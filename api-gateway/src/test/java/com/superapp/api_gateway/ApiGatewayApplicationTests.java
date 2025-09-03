package com.superapp.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiGatewayApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private WebTestClient webTestClient;

	@SuppressWarnings("resource")
	@Container
	@ServiceConnection
	static final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
			.withExposedPorts(6379);

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
