# Must-have (start here)
1. API Gateway – single entry point, routing, CORS, auth, rate limits
- Pick: Spring Cloud Gateway

2. Service Discovery – dynamic lookup between services
- Pick: Eureka (simple) → discovery-server app

3. Per-service Database – isolation of data
- You’re on MongoDB → one DB per service (e.g., userdb, orderdb)

4. Build & Packaging – consistent deps + containers
- Pick: Maven parent BOM (one “build-parent” POM) + Dockerfile per service

5. Local Orchestration – run everything together
- Pick: Docker Compose (Mongo(s) + discovery + gateway + services)

6. CI/CD – per-service pipeline & image registry
- Build → test → docker build → push (GitHub Actions/GitLab CI)
- Push to GHCR / Docker Hub / Nexus

7. Basic Observability – health, metrics, logs
- Add Spring Boot Actuator to every service
- Expose /actuator/health and /actuator/prometheus
- Log JSON to stdout (aggregatable later)

8. Security (JWT) – stateless auth at the edge
- Gateway validates JWT (opaque to downstream)
- IdP options: Keycloak (self-host), or cloud provider (Cognito/Okta)

# Production essentials (add next)

1. Centralized Config – keep config out of JARs
- Pick: Spring Cloud Config (git-backed) or just env vars + profiles

2. Resilience – timeouts/retries/circuit-breakers
- Pick: Resilience4j + Feign timeouts; bulkheads for hotspots

3. Distributed Tracing – follow a request across services
- Pick: OpenTelemetry + Jaeger/Tempo/Zipkin; propagate traceparent from gateway

4. Centralized Logs – search & alerting
- Pick: Fluent Bit/Filebeat → ELK/OpenSearch (later)

5. Secrets Management – rotate & audit
- Pick: Vault (or cloud secrets manager); mount as env vars

6. Async Messaging (when needed) – decouple workflows
- Pick: Kafka (high-throughput) or RabbitMQ (simple)
- Optional: Schema Registry for Kafka events

7. API Contracts & Docs – keep teams in sync
- Pick: OpenAPI (springdoc) per service; Pact (consumer-driven contracts)

8. Caching – offload hot reads / sessionless tokens
- Pick: Redis

9. Data Migrations – controlled schema/data changes
- Pick: Mongock (Mongo) or Flyway/Liquibase (SQL services)

10. Feature Flags – safe rollouts
- Pick: Unleash or OpenFeature

# “Nice to have” (later)

1. Rate limiting at gateway (token bucket rules per route)

2. Blue/Green / Canary deploys

3. Service templates (project generator)

4. Dev containers / Codespaces for consistent local envs
