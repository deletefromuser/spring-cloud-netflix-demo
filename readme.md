```shell
# /etc/hosts
# 127.0.0.1 cloud1.local
# 127.0.0.1 cloud2.local
sudo docker start test-mysql
sudo docker start gogs
# account/password: gogs/gogs
(cd config-server/ && mvn spring-boot:run)
# port 8888
(cd service-registry/ && mvn spring-boot:run -Dspring-boot.run.profiles=eureka-1)
# port 8761
(cd service-registry/ && mvn spring-boot:run -Dspring-boot.run.profiles=eureka-2)
# port 8762
(cd eureka-client/ && mvn spring-boot:run)
# port 0 #dynamic
(cd gateway-server/ && mvn spring-boot:run)
# port 8073
(cd eureka-client-consumer/ && mvn spring-boot:run)
# port 8081
```
