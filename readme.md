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


```bash
# refresh gateway routes
curl -s -X POST http://localhost:8073/actuator/refresh

```

### Docker
```bash
cd config-server/
sudo DOCKER_BUILDKIT=1 docker build -t spring-cloud-config-server .
sudo docker run -d --name spring-cloud-config-server_1 -v /home/vu18/.m2:/root/.m2 -p8888:8888 spring-cloud-config-server
sudo docker run -e TZ=Etc/GMT-8 -d --name spring-cloud-config-server_1 -v ${HOME}/.m2:/root/.m2 -v ${PWD}/logs:/logs -p8888:8888 spring-cloud-config-server

docker stop spring-cloud-config-server_1
docker rm spring-cloud-config-server_1

docker exec -it spring-cloud-config-server_1 bash

# build docker image with spring boot
mvn spring-boot:build-image -DskipTests
sudo docker run -d --name config-server_1 -e "SPRING_PROFILES_ACTIVE=docker"  -p8888:8888 config-server:0.0.1-SNAPSHOT
```

# docker compose
```
(cd config-server/ && mvn spring-boot:build-image -DskipTests)
(cd service-registry/ && mvn spring-boot:build-image -DskipTests)
(cd gateway-server/ && mvn spring-boot:build-image -DskipTests)
(cd eureka-client/ && mvn spring-boot:build-image -DskipTests)
(cd eureka-client-consumer/ && mvn spring-boot:build-image -DskipTests)

# monitor starting
watch -n 5 'docker ps -a | grep netflix'

# restart eureka-client only
git pull && (cd eureka-client/ && mvn spring-boot:build-image -DskipTests) && docker stop spring-cloud-netflix-demo-eureka-client-1 && docker rm spring-cloud-netflix-demo-eureka-client-1 && docker compose up -d --no-recreate

docker logs -f --tail 1000 spring-cloud-netflix-demo-eureka-client-1

```

# docker kafka
```
# https://hevodata.com/learn/kafka-cli-commands/#s25
# view topic
docker exec -it spring-cloud-netflix-demo-kafka-1 bash

/opt/bitnami/kafka/bin/kafka-topics.sh --list --bootstrap-server kafka:9092
/opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --describe --topic my-topic

/opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --delete --topic '.*'

/opt/bitnami/kafka/bin/kafka-console-producer.sh --bootstrap-server kafka:9092 --topic my-topic
/opt/bitnami/kafka/bin/kafka-console-producer.sh --bootstrap-server kafka:9092 --topic blah-in-0
{"title":"haha"}

/opt/bitnami/kafka/bin/kafka-console-consumer.sh --from-beginning --bootstrap-server kafka:9092 --topic my-topic
```

# edit docker instance file
```
docker exec -u 0 -it spring-cloud-netflix-demo-eureka-client-1 bash

apt-get update
apt-get install vim

vim /workspace/BOOT-INF/classes/application.yml

docker restart spring-cloud-netflix-demo-eureka-client-1
```

# find docker log 
```
du -chs /var/lib/docker/containers/*/*json.log

# clear log file
sudo sh -c 'truncate -s 0 /var/lib/docker/containers/*/*-json.log'
```



