spring cloud microservices

mvn clean package
cd :module && mvn spring-boot:run

eureka server: http://localhost:8761
api-gateway health check: http://localhost:8080/actuator/health


# Start minikube
minikube start
minikube addons enable ingress
kubectl create namespace demo
# (Optional) build images directly into minikube's docker daemon
mvn clean package -DskipTests
eval $(minikube -p minikube docker-env)
docker rmi user-service:local
docker rmi api-gateway:local
docker build -t user-service:local ./user-service
docker build -t api-gateway:local ./api-gateway

kubectl apply -f k8s/00-namespace.yaml
kubectl apply -f k8s/10-mongodb.yaml
kubectl apply -f k8s/12-redis.yaml
kubectl apply -f k8s/15-keycloak.yaml
kubectl apply -f k8s/20-user-service.yaml
kubectl apply -f k8s/30-api-gateway.yaml
kubectl apply -f k8s/40-ingress.yaml

kubectl rollout restart deploy/user-service -n demo
kubectl rollout restart deploy/api-gateway -n demo

kubectl get pods -n demo

MINIKUBE_IP=$(minikube ip)
echo "$MINIKUBE_IP api.local"  | sudo tee -a /etc/hosts
echo "$MINIKUBE_IP auth.local" | sudo tee -a /etc/hosts

# Example: if your user-service exposes GET /api/users/health
curl http://api.local/user/api/users/health

kubectl delete deployment mongodb -n demo
kubectl delete service mongodb -n demo
kubectl delete deployment redis -n demo
kubectl delete service redis -n demo
kubectl delete deployment user-service -n demo
kubectl delete service user-service -n demo
kubectl delete deployment api-gateway -n demo
kubectl delete service api-gateway -n demo

# expose gateway to computer
kubectl port-forward svc/api-gateway -n demo 8080:80