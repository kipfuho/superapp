# Spring Cloud Microservices

Project made using Spring Cloud, Docker, K8s...

## Basic command

- Build and run test: `mvn clean package`
- Run specific service: `mvn spring-boot:run`

## Local test

- Eureka server: http://localhost:8761
- api-gateway health check: http://localhost:8080/actuator/health

# Kubenetes

Simulating production deployments using minikube

## Start minikube

```
minikube start
minikube addons enable ingress
kubectl create namespace demo
```

## Build images directly into minikube's docker daemon

```
mvn clean package -DskipTests
eval $(minikube -p minikube docker-env)
docker rmi user-service:local
docker rmi api-gateway:local
docker build -t user-service:local ./user-service
docker build -t api-gateway:local ./api-gateway
```

## Apply configs

```
kubectl apply -f k8s/00-namespace.yaml
kubectl apply -f k8s/10-mongodb.yaml
kubectl apply -f k8s/12-redis.yaml
kubectl apply -f k8s/15-keycloak.yaml
kubectl apply -f k8s/20-user-service.yaml
kubectl apply -f k8s/30-api-gateway.yaml
kubectl apply -f k8s/40-ingress.yaml
```

## Useful commands

### Get pods

```
kubectl get pods -n demo
```

### Rollout after update config

```
kubectl rollout restart deploy/user-service -n demo
kubectl rollout restart deploy/api-gateway -n demo
```

### Delete service

```
kubectl delete deployment user-service -n demo
kubectl delete service user-service -n demo
```

### Expose service to localhost

```
kubectl port-forward svc/api-gateway -n demo 8080:80
```

### (Linux) Get minikube IP and map a host

```
MINIKUBE_IP=$(minikube ip)
echo "$MINIKUBE_IP api.local" | sudo tee -a /etc/hosts
echo "$MINIKUBE_IP auth.local" | sudo tee -a /etc/hosts

curl http://api.local/actuator/health
curl http://api.local/api/user-service/actuator/health
```

# Local databases

### Create PostgreSQLS

```
docker run -d \
  --name postgresql \
  -e POSTGRESQL_USERNAME=root \
  -e POSTGRESQL_PASSWORD=secret \
  --restart unless-stopped \
  -p 5432:5432 \
  -v postgresql_data:/bitnami/postgresql \
  bitnami/postgresql:latest
```

### Create MongoDB

```
docker run -d \
  --name mongodb \
  -e MONGODB_USERNAME=root \
  -e MONGODB_PASSWORD=secret \
  -e MONGODB_ROOT_PASSWORD=secret \
  --restart unless-stopped \
  -p 27017:27017 \
  -v mongodb_data:/bitnami/mongodb \
  bitnami/mongodb:latest
```

### Create Redis

```
docker run -d \
  --name redis \
  --restart unless-stopped \
  -p 6379:6379 \
  -v redis_data:/bitnami/redis \
  bitnami/redis:latest
```
