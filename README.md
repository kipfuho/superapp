spring cloud microservices

mvn clean package
cd :module && mvn spring-boot:run

eureka server: http://localhost:8761
api-gateway health check: http://localhost:8080/actuator/health