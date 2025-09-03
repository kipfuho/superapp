package com.superapp.user_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class UserServiceApplicationTests {

	@Container
	@ServiceConnection // Boot sets spring.data.mongodb.uri and waits for readiness
	static final MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse("mongo:7"));

	@SuppressWarnings("resource")
	@Container
	@ServiceConnection // Boot sets Redis host/port for Spring Data Redis
	static final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
			.withExposedPorts(6379);

	@Test
	void contextLoads() {
	}

}
