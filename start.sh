sudo docker start test-mysql
sudo docker start gogs
gnome-terminal -x bash -c "(cd config-server/ && mvn spring-boot:run); exec bash"
sleep 15
gnome-terminal -x bash -c "(cd service-registry/ && mvn spring-boot:run -Dspring-boot.run.profiles=eureka-1); exec bash"
sleep 15
gnome-terminal -x bash -c "(cd service-registry/ && mvn spring-boot:run -Dspring-boot.run.profiles=eureka-2); exec bash"
sleep 15
gnome-terminal -x bash -c "(cd eureka-client/ && mvn spring-boot:run); exec bash"
sleep 15
gnome-terminal -x bash -c "(cd gateway-server/ && mvn spring-boot:run); exec bash"
sleep 15
gnome-terminal -x bash -c "(cd eureka-client-consumer/ && mvn spring-boot:run); exec bash"








