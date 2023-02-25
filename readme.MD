1. docker network create nelf-network
2. docker run --name nelf_mysql --network=nelf-network -p 127.0.0.1:7777:3306 \
   -e MYSQL_ROOT_PASSWORD=bestuser \
   -e MYSQL_USER=bestuser \
   -e MYSQL_PASSWORD=bestuser \
   -e MYSQL_DATABASE=telegram_bot \
   -d mysql:latest
3. docker run --name nelf_weatherbot --network=nelf-network -p 127.0.0.1:7775:8080 nelfy/weatherbot:1