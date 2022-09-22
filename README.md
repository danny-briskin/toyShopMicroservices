docker network create micros-toyshop

docker build -t micros-eureka .
docker build -t micros-servman .
docker build -t micros-toyshopserv .
docker build -t micros-toyshopcustadd-1 .
docker build -t micros-toyshopcustadd-2 .




docker run --name c-micros-eureka -p 7000:7000 --network=micros-toyshop --rm micros-eureka
docker run --name c-micros-servman -p 8081:8081 --network=micros-toyshop --rm -e EUREKA_ZONE=http://discUser:discPassword@c-micros-eureka:7000/eureka/ micros-servman
docker run --name c-micros-toyshopserv -p 7070:7070 --network=micros-toyshop --rm -e EUREKA_ZONE=http://discUser:discPassword@c-micros-eureka:7000/eureka/ micros-toyshopserv
docker run --name c-micros-toyshopcustadd-1 -p 7081:7081 --network=micros-toyshop --rm -e EUREKA_ZONE=http://discUser:discPassword@c-micros-eureka:7000/eureka/ -e SERVER_CONT=c-micros-toyshopcustadd-1 micros-toyshopcustadd-1
docker run --name c-micros-toyshopcustadd-2 -p 7082:7082 --network=micros-toyshop --rm -e EUREKA_ZONE=http://discUser:discPassword@c-micros-eureka:7000/eureka/ -e SERVER_CONT=c-micros-toyshopcustadd-2 micros-toyshopcustadd-2

