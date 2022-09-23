# Docker + Eureka
Network
`docker network create micros-toyshop`

Build images
`docker build -t micros-eureka .`

`docker build -t micros-servman .`

`docker build -t micros-toyshopserv .`

`docker build -t micros-toyshopcustadd-1 .`

`docker build -t micros-toyshopcustadd-2 .`

Single service for K8ts only

`docker build -t customer-additional-service .`


Run containers
`docker run --name c-micros-eureka -p 7000:7000 --network=micros-toyshop --rm micros-eureka`

`docker run --name c-micros-servman -p 8081:8081 --network=micros-toyshop --rm -e EUREKA_ZONE=http://discUser:discPassword@c-micros-eureka:7000/eureka/ micros-servman`

`docker run --name c-micros-toyshopserv -p 7070:7070 --network=micros-toyshop --rm -e EUREKA_ZONE=http://discUser:discPassword@c-micros-eureka:7000/eureka/ micros-toyshopserv`

`docker run --name c-micros-toyshopcustadd-1 -p 7081:7081 --network=micros-toyshop --rm -e EUREKA_ZONE=http://discUser:discPassword@c-micros-eureka:7000/eureka/ -e SERVER_CONT=c-micros-toyshopcustadd-1 micros-toyshopcustadd-1`

`docker run --name c-micros-toyshopcustadd-2 -p 7082:7082 --network=micros-toyshop --rm -e EUREKA_ZONE=http://discUser:discPassword@c-micros-eureka:7000/eureka/ -e SERVER_CONT=c-micros-toyshopcustadd-2 micros-toyshopcustadd-2`

# Kubernetes
## Start
`sudo service docker start`

`minikube start`

`kubectl cluster-info`

Dashboard 
`minikube dashboard`

Apply docker config (to be able to add containers into k8ts)

`eval $(minikube docker-env)`

`kubectl get pod -A`

## Deployments
Apply all yaml configs from "kube" folder
`kubectl apply -f kube`

`kubectl delete deploy micros-eureka`

## Services
List of services
`kubectl get svc`

`minikube service --all`

Delete service
`kubectl delete svc micros-eureka`

Restart service
`kubectl rollout restart deployment micros-toyshopserv`

Install service


`minikube service micros-toyshopserv`

`minikube service customer-additional-service`

## Scale
`kubectl scale --replicas=5 deployment/customer-additional-service`

