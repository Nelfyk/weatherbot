docker stop mysql
docker rm mysql
docker network create nelf-network
docker run --name mysql --network=host -p 7777:3306 \
-e MYSQL_ROOT_PASSWORD=bestuser \
-e MYSQL_USER=bestuser \
-e MYSQL_PASSWORD=bestuser \
-e MYSQL_DATABASE=weather_bot \
-d mysql:latest